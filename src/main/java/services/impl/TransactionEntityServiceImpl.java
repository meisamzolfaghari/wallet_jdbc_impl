package services.impl;

import config.ConnectionFactory;
import entities.Transaction;
import repos.TransactionRepository;
import services.TransactionService;
import services.exception.EntityNotFoundException;
import services.exception.TransactionServiceException;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionEntityServiceImpl extends BaseEntityServiceImpl<String, Transaction, TransactionRepository> implements TransactionService {

    private TransactionEntityServiceImpl() {
    }

    private static final class TransactionServiceImplHolder {
        private static final TransactionEntityServiceImpl TRANSACTION_SERVICE_IMPL = new TransactionEntityServiceImpl();
    }

    public static TransactionEntityServiceImpl getInstance() {
        return TransactionServiceImplHolder.TRANSACTION_SERVICE_IMPL;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) throws TransactionServiceException {
        try (Connection connection = ConnectionFactory.getConnection()) {
            if (transaction != null) {
                getRepository().create(transaction, connection);
                connection.commit();
            }
            return transaction;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TransactionServiceException("error on creating transaction!");
        }
    }

    @Override
    public Transaction getById(String transactionId) throws TransactionServiceException, EntityNotFoundException {
        try (Connection connection = ConnectionFactory.getConnection()) {
            return getRepository().findById(transactionId, connection).orElseThrow(() ->
                    new EntityNotFoundException("entity not found with id: " + transactionId));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TransactionServiceException("error on getting entity for id: " + transactionId);
        }
    }

    @Override
    protected TransactionRepository getRepository() {
        return TransactionRepository.getInstance();
    }
}
