package services.impl;

import config.ConnectionFactory;
import entities.Transaction;
import entities.TransactionStatus;
import entities.Wallet;
import repos.TransactionRepository;
import repos.WalletRepository;
import services.TransferService;
import services.exception.EntityNotFoundException;
import services.exception.TransferServiceException;
import services.exception.WalletServiceException;

import java.sql.Connection;
import java.sql.SQLException;

public class TransferServiceImpl implements TransferService {

    private TransferServiceImpl() {
    }

    private static final class TransferServiceImplHolder {
        private static final TransferServiceImpl TRANSFER_SERVICE_IMPL = new TransferServiceImpl();
    }

    public static TransferServiceImpl getInstance() {
        return TransferServiceImplHolder.TRANSFER_SERVICE_IMPL;
    }

    private final WalletRepository walletRepository = WalletRepository.getInstance();
    private final TransactionRepository transactionRepository = TransactionRepository.getInstance();

    @Override
    public void transferMoney(Transaction transaction) throws TransferServiceException {

        try (Connection connection = ConnectionFactory.getConnection()) {

            Wallet senderWallet = getWalletById(transaction.getSenderWalletId(), connection);
            Wallet receiverWallet = getWalletById(transaction.getReceiverWalletId(), connection);

            if (senderWallet.getBalance() < transaction.getAmount())
                throw new WalletServiceException(String.format("sender wallet: %s does not have " +
                                "enough money to transfer! transfer to receiver wallet: %s . amount: %s",
                        senderWallet.getId(), receiverWallet.getId(), transaction.getAmount()));

            senderWallet.setBalance(senderWallet.getBalance() - transaction.getAmount());
            receiverWallet.setBalance(receiverWallet.getBalance() + transaction.getAmount());

            walletRepository.update(senderWallet, connection);
            walletRepository.update(receiverWallet, connection);

            successTransaction(transaction, connection);

            connection.commit();

        } catch (Exception exception) {
            final String message = String.format("error on transferring money! " +
                            "{sender wallet: %s, receiver wallet: %s, amount: %s} ",
                    transaction.getSenderWalletId(), transaction.getReceiverWalletId(), transaction.getAmount());

            exception.printStackTrace();

            failTransaction(transaction);

            throw new TransferServiceException(message);
        }
    }

    private Wallet getWalletById(Long walletId, Connection connection) throws EntityNotFoundException, SQLException {
        return walletRepository.findById(walletId, connection)
                .orElseThrow(() -> new EntityNotFoundException("wallet not found for id: " + walletId));
    }

    @Override
    public void depositMoney(Transaction transaction) throws TransferServiceException {

        try (Connection connection = ConnectionFactory.getConnection()) {

            Wallet wallet = getWalletById(transaction.getReceiverWalletId(), connection);

            wallet.setBalance(wallet.getBalance() + transaction.getAmount());

            walletRepository.update(wallet, connection);

            successTransaction(transaction, connection);

            connection.commit();

        } catch (Exception exception) {
            final String message = String.format("error on deposit money! {receiver wallet: %s, amount: %s} ",
                    transaction.getReceiverWalletId(), transaction.getAmount());

            exception.printStackTrace();

            failTransaction(transaction);

            throw new TransferServiceException(message);
        }

    }

    @Override
    public void withdrawMoney(Transaction transaction) throws TransferServiceException {

        try (Connection connection = ConnectionFactory.getConnection()) {

            Wallet wallet = getWalletById(transaction.getSenderWalletId(), connection);

            if (wallet.getBalance() < transaction.getAmount())
                throw new TransferServiceException(String.format("wallet: %s does not have " +
                                "enough money to withdraw! amount: %s",
                        wallet.getId(), transaction.getAmount()));

            wallet.setBalance(wallet.getBalance() - transaction.getAmount());

            walletRepository.update(wallet, connection);

            successTransaction(transaction, connection);

        } catch (Exception exception) {
            final String message = String.format("error on transferring money! " +
                            "{sender wallet: %s, receiver wallet: %s, amount: %s} ",
                    transaction.getSenderWalletId(), transaction.getReceiverWalletId(), transaction.getAmount());

            exception.printStackTrace();

            failTransaction(transaction);

            throw new TransferServiceException(message);
        }
    }

    private void successTransaction(Transaction transaction, Connection connection) throws SQLException {
        transaction.setStatus(TransactionStatus.SUCCESS.name());
        transactionRepository.update(transaction, connection);
    }

    private void failTransaction(Transaction transaction) {

        try (Connection connection = ConnectionFactory.getConnection()) {

            transaction.setStatus(TransactionStatus.FAILED.name());
            transactionRepository.update(transaction, connection);
            connection.commit();

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.printf("error on failing transaction with id: %s", transaction.getId());
        }
    }
}
