package services.impl;

import entities.*;
import entities.dto.MoneyTransferDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repos.TransactionRepository;
import repos.UserRepository;
import repos.WalletRepository;
import services.WalletService;
import services.exception.UserServiceException;
import services.exception.WalletServiceException;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WalletServiceImpl extends BaseServiceImpl<Integer, Wallet, WalletRepository> implements WalletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletServiceImpl.class);

    private static final class WalletServiceImplHolder {
        private static final WalletServiceImpl WALLET_SERVICE_IMPL = new WalletServiceImpl();
    }

    public static WalletServiceImpl getInstance() {
        return WalletServiceImplHolder.WALLET_SERVICE_IMPL;
    }


    @Override
    public Wallet getWallet(Integer walletId) throws WalletServiceException {
        try {

            return getById(walletId);

        } catch (UserServiceException e) {
            final String message = String.format("error on getting wallet for id: %s ", walletId);
            LOGGER.error(message, e);
            throw new WalletServiceException(message);
        }
    }

    private Wallet getUserWalletByUsername(String username) throws SQLException, WalletServiceException {
        Integer walletId = UserRepository.getInstance()
                .findByUsername(username)
                .orElseThrow().getWalletId();
        return getWallet(walletId);
    }

    @Override
    public String moneyTransferToOtherUser(MoneyTransferDetails moneyTransferDetails) throws WalletServiceException {
        try {

            Wallet senderWallet = getUserWalletByUsername(moneyTransferDetails.getSenderUsername());

            Wallet receiverWallet = getUserWalletByUsername(moneyTransferDetails.getReceiverUsername());

//            Transaction receiverTransaction = new Transaction(UUID.randomUUID().toString(), new Date(),
//                    TransactionStatus.NEW.name(), TransactionType.CREDIT.name(),
//                    moneyTransferDetails.getAmount(), receiverWallet.getId());

            Transaction transaction = new Transaction(UUID.randomUUID().toString(), new Date(),
                    TransactionStatus.NEW.name(), TransactionType.USER_TO_USER.name(),
                    moneyTransferDetails.getAmount(), senderWallet.getId(), receiverWallet.getId());

            TransactionRepository.getInstance().create(transaction);

            getRepository().getConnection().commit();

            CompletableFuture.runAsync(() ->
            {
                try {
                    transferMoney(transaction, moneyTransferDetails.getSenderUsername(),
                            moneyTransferDetails.getReceiverUsername(), moneyTransferDetails.getAmount());
                } catch (Exception e) {
                    LOGGER.error("error on transferring money! ", e);
                }
            });

            return transaction.getId();

        } catch (SQLException e) {
            final String message = String.format("error on creating sender transaction {receiver: %s, sender: %s, amount: %s}",
                    moneyTransferDetails.getReceiverUsername(), moneyTransferDetails.getSenderUsername(),
                    moneyTransferDetails.getAmount());
            LOGGER.error(message, e);
            throw new WalletServiceException(message);
        }
    }

    private void transferMoney(Transaction transaction, String senderUsername,
                               String receiverUsername, Integer amount) throws SQLException, WalletServiceException {
        try {

            Wallet senderWallet = getUserWalletByUsername(senderUsername);
            Wallet receiverWallet = getUserWalletByUsername(receiverUsername);

            if (senderWallet.getBalance() < amount)
                throw new WalletServiceException(String.format("sender: %s does not have enough money to transfer! " +
                        "transfer to receiver: %s . amount: %s", receiverUsername, senderUsername, amount));

            senderWallet.setBalance(senderWallet.getBalance() - amount);
            receiverWallet.setBalance(receiverWallet.getBalance() + amount);

            getRepository().update(senderWallet);
            getRepository().update(receiverWallet);

            transaction.setStatus(TransactionStatus.SUCCESS.name());
            TransactionRepository.getInstance().update(transaction);
            getRepository().getConnection().commit();

        } catch (Exception exception) {
            final String message = String.format("error on transferring money! {sender: %s, receiver: %s, amount: %s} "
                    , senderUsername, receiverUsername, amount);
            LOGGER.error(message, exception);
            transaction.setStatus(TransactionStatus.FAILED.name());
            TransactionRepository.getInstance().update(transaction);
            throw new WalletServiceException(message);
        }
    }

    @Override
    protected WalletRepository getRepository() {
        return WalletRepository.getInstance();
    }
}
