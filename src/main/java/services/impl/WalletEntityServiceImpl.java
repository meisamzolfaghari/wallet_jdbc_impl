package services.impl;

import entities.*;
import services.dto.MoneyDepositWithdrawDetails;
import services.dto.MoneyTransferDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repos.TransactionRepository;
import repos.UserRepository;
import repos.WalletRepository;
import services.WalletService;
import services.exception.UserNotFoundException;
import services.exception.WalletServiceException;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WalletEntityServiceImpl extends BaseEntityServiceImpl<Integer, Wallet, WalletRepository> implements WalletService {

    private WalletEntityServiceImpl() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletEntityServiceImpl.class);

    private static final class WalletServiceImplHolder {
        private static final WalletEntityServiceImpl WALLET_SERVICE_IMPL = new WalletEntityServiceImpl();
    }

    public static WalletEntityServiceImpl getInstance() {
        return WalletServiceImplHolder.WALLET_SERVICE_IMPL;
    }


    private Wallet getUserWalletByUsername(String username) throws SQLException, UserNotFoundException {
        Integer walletId = UserRepository.getInstance()
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("user not found for username: " + username))
                .getWalletId();
        return getById(walletId);
    }

    @Override
    public String moneyTransferToOtherUser(MoneyTransferDetails moneyTransferDetails) throws WalletServiceException, UserNotFoundException {
        try {

            Wallet senderWallet = getUserWalletByUsername(moneyTransferDetails.getSenderUsername());

            Wallet receiverWallet = getUserWalletByUsername(moneyTransferDetails.getReceiverUsername());

            Transaction transaction = new Transaction(UUID.randomUUID().toString(), new Date(),
                    TransactionStatus.NEW.name(), TransactionType.USER_TO_USER.name(),
                    moneyTransferDetails.getAmount(), senderWallet.getId(), receiverWallet.getId());

            TransactionRepository.getInstance().create(transaction);

            getRepository().getConnection().commit();

            CompletableFuture.runAsync(() ->
            {
                try {
                    TransferServiceImpl.getInstance().transferMoney(transaction);
                } catch (Exception e) {
                    LOGGER.error("error on transferring money! ", e);
                }
            });

            return transaction.getId();

        } catch (SQLException e) {
            final String message = String.format("error on creating transfer transaction {receiver: %s, sender: %s, amount: %s}",
                    moneyTransferDetails.getReceiverUsername(), moneyTransferDetails.getSenderUsername(),
                    moneyTransferDetails.getAmount());
            LOGGER.error(message, e);
            throw new WalletServiceException(message);
        }
    }

    @Override
    public String deposit(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails)
            throws WalletServiceException, UserNotFoundException {

        try {

            Wallet wallet = getUserWalletByUsername(moneyDepositWithdrawDetails.getUsername());

            Transaction transaction = new Transaction(UUID.randomUUID().toString(), new Date(),
                    TransactionStatus.NEW.name(), TransactionType.DEPOSIT.name(),
                    moneyDepositWithdrawDetails.getAmount(), wallet.getId(), null);

            TransactionRepository.getInstance().create(transaction);

            getRepository().getConnection().commit();

            CompletableFuture.runAsync(() ->
            {
                try {
                    TransferServiceImpl.getInstance().depositMoney(transaction);
                } catch (Exception e) {
                    LOGGER.error("error on deposit money! ", e);
                }
            });

            return transaction.getId();

        } catch (SQLException e) {
            final String message = String.format("error on creating deposit transaction {receiver: %s, amount: %s}",
                    moneyDepositWithdrawDetails.getUsername(), moneyDepositWithdrawDetails.getAmount());
            LOGGER.error(message, e);
            throw new WalletServiceException(message);
        }
    }

    @Override
    public String withdraw(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails)
            throws WalletServiceException, UserNotFoundException {

        try {

            Wallet wallet = getUserWalletByUsername(moneyDepositWithdrawDetails.getUsername());

            Transaction transaction = new Transaction(UUID.randomUUID().toString(), new Date(),
                    TransactionStatus.NEW.name(), TransactionType.WITHDRAW.name(),
                    moneyDepositWithdrawDetails.getAmount(), wallet.getId(), null);

            TransactionRepository.getInstance().create(transaction);

            getRepository().getConnection().commit();

            CompletableFuture.runAsync(() ->
            {
                try {
                    TransferServiceImpl.getInstance().withdrawMoney(transaction);
                } catch (Exception e) {
                    LOGGER.error("error on withdraw money! ", e);
                }
            });

            return transaction.getId();

        } catch (SQLException e) {
            final String message = String.format("error on creating withdraw transaction {sender: %s, amount: %s}",
                    moneyDepositWithdrawDetails.getUsername(), moneyDepositWithdrawDetails.getAmount());
            LOGGER.error(message, e);
            throw new WalletServiceException(message);
        }
    }

    @Override
    protected WalletRepository getRepository() {
        return WalletRepository.getInstance();
    }
}
