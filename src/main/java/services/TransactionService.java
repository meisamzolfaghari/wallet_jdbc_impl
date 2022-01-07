package services;

import entities.Transaction;
import services.exception.TransactionServiceException;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction) throws TransactionServiceException;

    Transaction getTransaction(String transactionId) throws TransactionServiceException;

}
