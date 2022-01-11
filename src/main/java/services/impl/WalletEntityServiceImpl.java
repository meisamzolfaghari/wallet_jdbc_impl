package services.impl;

import config.ConnectionFactory;
import entities.Transaction;
import entities.TransactionStatus;
import entities.TransactionType;
import entities.Wallet;
import repos.TransactionRepository;
import repos.UserRepository;
import repos.WalletRepository;
import services.WalletService;
import services.dto.MoneyDepositWithdrawDetails;
import services.dto.MoneyTransferDetails;
import services.exception.EntityNotFoundException;
import services.exception.WalletServiceException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class WalletEntityServiceImpl extends BaseEntityServiceImpl<Long, Wallet, WalletRepository> implements WalletService {

    private WalletEntityServiceImpl() {
    }

    private final UserRepository userRepository = UserRepository.getInstance();
    private final TransactionRepository transactionRepository = TransactionRepository.getInstance();

    private static final class WalletServiceImplHolder {
        private static final WalletEntityServiceImpl WALLET_SERVICE_IMPL = new WalletEntityServiceImpl();
    }

    public static WalletEntityServiceImpl getInstance() {
        return WalletServiceImplHolder.WALLET_SERVICE_IMPL;
    }


    private Wallet getUserWalletByUsername(String username, Connection connection) throws SQLException, EntityNotFoundException {
        if (connection == null)
            connection = ConnectionFactory.getConnection();

        Long walletId = userRepository.findByUsername(username, connection)
                .orElseThrow(() -> new EntityNotFoundException("user not found for username: " + username))
                .getWalletId();

        return getRepository().findById(walletId, connection)
                .orElseThrow(() -> new EntityNotFoundException("wallet not found for id: " + walletId));
    }

    @Override
    public Wallet getById(Long walletId) throws WalletServiceException, EntityNotFoundException {

        try (Connection connection = ConnectionFactory.getConnection()) {

            return getRepository().findById(walletId, connection).orElseThrow(() ->
                    new EntityNotFoundException("wallet not found with id: " + walletId));

        } catch (SQLException e) {
            e.printStackTrace();
            throw new WalletServiceException("error on getting wallet for id: " + walletId);
        }
    }

    @Override
    public String moneyTransferToOtherUser(MoneyTransferDetails moneyTransferDetails) throws WalletServiceException, EntityNotFoundException {

        try (Connection connection = ConnectionFactory.getConnection()) {

            Wallet senderWallet = getUserWalletByUsername(moneyTransferDetails.getSenderUsername(), connection);

            Wallet receiverWallet = getUserWalletByUsername(moneyTransferDetails.getReceiverUsername(), connection);

            Transaction transaction = new Transaction(UUID.randomUUID().toString(), new Date(),
                    TransactionStatus.NEW.name(), TransactionType.USER_TO_USER.name(),
                    moneyTransferDetails.getAmount(), senderWallet.getId(), receiverWallet.getId());

            transactionRepository.create(transaction, connection);

            connection.commit();

            CompletableFuture.runAsync(() ->
            {
                try {
                    TransferServiceImpl.getInstance().transferMoney(transaction);
                } catch (Exception e) {
                    System.out.println("error on transferring money! ");
                    e.printStackTrace();
                }
            });

            return transaction.getId();

        } catch (SQLException e) {
            e.printStackTrace();

            final String message = String.format("error on creating transfer transaction {receiver: %s, sender: %s, amount: %s}",
                    moneyTransferDetails.getReceiverUsername(), moneyTransferDetails.getSenderUsername(),
                    moneyTransferDetails.getAmount());
            throw new WalletServiceException(message);
        }
    }

    @Override
    public String deposit(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails)
            throws WalletServiceException, EntityNotFoundException {

        try {
            Connection connection = ConnectionFactory.getConnection();

            Wallet wallet = getUserWalletByUsername(moneyDepositWithdrawDetails.getUsername(), connection);

            Transaction transaction = new Transaction(UUID.randomUUID().toString(), new Date(),
                    TransactionStatus.NEW.name(), TransactionType.DEPOSIT.name(),
                    moneyDepositWithdrawDetails.getAmount(), null, wallet.getId());

            transactionRepository.create(transaction, connection);

            connection.commit();

            CompletableFuture.runAsync(() ->
            {
                try {
                    TransferServiceImpl.getInstance().depositMoney(transaction);
                } catch (Exception e) {
                    System.out.println("error on deposit money! ");
                    e.printStackTrace();
                }
            });

            return transaction.getId();

        } catch (SQLException e) {
            e.printStackTrace();

            final String message = String.format("error on creating deposit transaction {receiver: %s, amount: %s}",
                    moneyDepositWithdrawDetails.getUsername(), moneyDepositWithdrawDetails.getAmount());
            throw new WalletServiceException(message);
        }
    }

    @Override
    public String withdraw(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails)
            throws WalletServiceException, EntityNotFoundException {

        try {
            Connection connection = ConnectionFactory.getConnection();

            Wallet wallet = getUserWalletByUsername(moneyDepositWithdrawDetails.getUsername(), connection);

            Transaction transaction = new Transaction(UUID.randomUUID().toString(), new Date(),
                    TransactionStatus.NEW.name(), TransactionType.WITHDRAW.name(),
                    moneyDepositWithdrawDetails.getAmount(), wallet.getId(), null);

            transactionRepository.create(transaction, connection);

            connection.commit();

            CompletableFuture.runAsync(() ->
            {
                try {
                    TransferServiceImpl.getInstance().withdrawMoney(transaction);
                } catch (Exception e) {
                    System.out.println("error on withdraw money! ");
                    e.printStackTrace();
                }
            });

            return transaction.getId();

        } catch (SQLException e) {
            e.printStackTrace();
            final String message = String.format("error on creating withdraw transaction {sender: %s, amount: %s}",
                    moneyDepositWithdrawDetails.getUsername(), moneyDepositWithdrawDetails.getAmount());
            throw new WalletServiceException(message);
        }
    }

    @Override
    protected WalletRepository getRepository() {
        return WalletRepository.getInstance();
    }
}
