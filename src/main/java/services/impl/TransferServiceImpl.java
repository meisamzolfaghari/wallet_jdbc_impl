package services.impl;

import entities.Transaction;
import entities.TransactionStatus;
import entities.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repos.TransactionRepository;
import services.TransferService;
import services.exception.TransferServiceException;
import services.exception.WalletServiceException;

import java.sql.SQLException;

public class TransferServiceImpl implements TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferServiceImpl.class);

    private static final class TransferServiceImplHolder {
        private static final TransferServiceImpl TRANSFER_SERVICE_IMPL = new TransferServiceImpl();
    }

    public static TransferServiceImpl getInstance() {
        return TransferServiceImplHolder.TRANSFER_SERVICE_IMPL;
    }

    private final WalletEntityServiceImpl walletService = WalletEntityServiceImpl.getInstance();
    private final TransactionRepository transactionRepository = TransactionRepository.getInstance();

    @Override
    public void transferMoney(Transaction transaction) throws TransferServiceException {
        try {

            Wallet senderWallet = walletService.getById(transaction.getSenderWalletId());
            Wallet receiverWallet = walletService.getById(transaction.getReceiverWalletId());

            if (senderWallet.getBalance() < transaction.getAmount())
                throw new WalletServiceException(String.format("sender wallet: %s does not have " +
                                "enough money to transfer! transfer to receiver wallet: %s . amount: %s",
                        senderWallet.getId(), receiverWallet.getId(), transaction.getAmount()));

            senderWallet.setBalance(senderWallet.getBalance() - transaction.getAmount());
            receiverWallet.setBalance(receiverWallet.getBalance() + transaction.getAmount());

            walletService.getRepository().update(senderWallet);
            walletService.getRepository().update(receiverWallet);

            successTransaction(transaction);

        } catch (Exception exception) {
            final String message = String.format("error on transferring money! " +
                            "{sender wallet: %s, receiver wallet: %s, amount: %s} ",
                    transaction.getSenderWalletId(), transaction.getReceiverWalletId(), transaction.getAmount());

            LOGGER.error(message, exception);

            failTransaction(transaction);

            throw new TransferServiceException(message);
        }
    }

    @Override
    public void depositMoney(Transaction transaction) throws TransferServiceException {

        try {

            Wallet wallet = walletService.getById(transaction.getReceiverWalletId());

            wallet.setBalance(wallet.getBalance() + transaction.getAmount());

            walletService.getRepository().update(wallet);

            successTransaction(transaction);
        } catch (Exception exception) {
            final String message = String.format("error on deposit money! {receiver wallet: %s, amount: %s} ",
                    transaction.getReceiverWalletId(), transaction.getAmount());

            LOGGER.error(message, exception);

            failTransaction(transaction);

            throw new TransferServiceException(message);
        }

    }

    @Override
    public void withdrawMoney(Transaction transaction) throws TransferServiceException {
        try {

            Wallet wallet = walletService.getById(transaction.getSenderWalletId());

            if (wallet.getBalance() < transaction.getAmount())
                throw new TransferServiceException(String.format("wallet: %s does not have " +
                                "enough money to withdraw! amount: %s",
                        wallet.getId(), transaction.getAmount()));

            wallet.setBalance(wallet.getBalance() - transaction.getAmount());

            walletService.getRepository().update(wallet);

            successTransaction(transaction);

        } catch (Exception exception) {
            final String message = String.format("error on transferring money! " +
                            "{sender wallet: %s, receiver wallet: %s, amount: %s} ",
                    transaction.getSenderWalletId(), transaction.getReceiverWalletId(), transaction.getAmount());

            LOGGER.error(message, exception);

            failTransaction(transaction);

            throw new TransferServiceException(message);
        }
    }

    private void successTransaction(Transaction transaction) throws SQLException {
        transaction.setStatus(TransactionStatus.SUCCESS.name());
        transactionRepository.update(transaction);
        transactionRepository.getConnection().commit();
    }

    private void failTransaction(Transaction transaction) {
        try {
            transaction.setStatus(TransactionStatus.FAILED.name());
            transactionRepository.update(transaction);
            transactionRepository.getConnection().commit();
        } catch (SQLException exception) {
            LOGGER.error("error on failing transaction with id: {}", transaction.getId(), exception);
        }
    }
}
