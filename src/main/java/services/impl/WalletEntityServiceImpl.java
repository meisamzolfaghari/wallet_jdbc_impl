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
import services.exception.WalletServiceException;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WalletEntityServiceImpl extends BaseEntityServiceImpl<Integer, Wallet, WalletRepository> implements WalletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletEntityServiceImpl.class);

    private static final class WalletServiceImplHolder {
        private static final WalletEntityServiceImpl WALLET_SERVICE_IMPL = new WalletEntityServiceImpl();
    }

    public static WalletEntityServiceImpl getInstance() {
        return WalletServiceImplHolder.WALLET_SERVICE_IMPL;
    }


    private Wallet getUserWalletByUsername(String username) throws SQLException, WalletServiceException {
        Integer walletId = UserRepository.getInstance()
                .findByUsername(username)
                .orElseThrow().getWalletId();
        return getById(walletId);
    }

    @Override
    public String moneyTransferToOtherUser(MoneyTransferDetails moneyTransferDetails) throws WalletServiceException {
        try {

            Wallet senderWallet = getById(moneyTransferDetails.getSenderWalletId());

            Wallet receiverWallet = getById(moneyTransferDetails.getReceiverWalletId());

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
                    moneyTransferDetails.getReceiverWalletId(), moneyTransferDetails.getSenderWalletId(),
                    moneyTransferDetails.getAmount());
            LOGGER.error(message, e);
            throw new WalletServiceException(message);
        }
    }

    @Override
    public String deposit(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails) throws WalletServiceException {
        try {

            Wallet wallet = getById(moneyDepositWithdrawDetails.getWalletId());

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
                    moneyDepositWithdrawDetails.getWalletId(), moneyDepositWithdrawDetails.getAmount());
            LOGGER.error(message, e);
            throw new WalletServiceException(message);
        }
    }

    @Override
    public String withdraw(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails) throws WalletServiceException {
        try {

            Wallet wallet = getById(moneyDepositWithdrawDetails.getWalletId());

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
                    moneyDepositWithdrawDetails.getWalletId(), moneyDepositWithdrawDetails.getAmount());
            LOGGER.error(message, e);
            throw new WalletServiceException(message);
        }
    }

    @Override
    protected WalletRepository getRepository() {
        return WalletRepository.getInstance();
    }
}
