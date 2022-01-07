package services.impl;

import entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repos.TransactionRepository;
import services.TransactionService;
import services.exception.TransactionServiceException;
import services.exception.UserServiceException;

import java.sql.SQLException;

public class TransactionServiceImpl extends BaseServiceImpl<String, Transaction, TransactionRepository> implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private static final class TransactionServiceImplHolder {
        private static final TransactionServiceImpl TRANSACTION_SERVICE_IMPL = new TransactionServiceImpl();
    }

    public static TransactionServiceImpl getInstance() {
        return TransactionServiceImplHolder.TRANSACTION_SERVICE_IMPL;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) throws TransactionServiceException {
        try {
            if (transaction != null) {
                getRepository().create(transaction);
                getRepository().getConnection().commit();
            }
            return transaction;
        } catch (SQLException e) {
            final String message = "error on creating transaction! ";
            LOGGER.error(message, e);
            throw new TransactionServiceException(message);
        }
    }

    @Override
    public Transaction getTransaction(String transactionId) throws TransactionServiceException {
        try {

            return getById(transactionId);

        } catch (UserServiceException e) {
            final String message = String.format("error on getting transaction for id: %s ", transactionId);
            LOGGER.error(message, e);
            throw new TransactionServiceException(message);
        }
    }

    @Override
    protected TransactionRepository getRepository() {
        return TransactionRepository.getInstance();
    }
}
