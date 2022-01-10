package services.impl;

import entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repos.TransactionRepository;
import services.TransactionService;
import services.exception.TransactionServiceException;

import java.sql.SQLException;

public class TransactionEntityServiceImpl extends BaseEntityServiceImpl<String, Transaction, TransactionRepository> implements TransactionService {

    private TransactionEntityServiceImpl() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionEntityServiceImpl.class);

    private static final class TransactionServiceImplHolder {
        private static final TransactionEntityServiceImpl TRANSACTION_SERVICE_IMPL = new TransactionEntityServiceImpl();
    }

    public static TransactionEntityServiceImpl getInstance() {
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
    protected TransactionRepository getRepository() {
        return TransactionRepository.getInstance();
    }
}
