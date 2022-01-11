package services;

import entities.Transaction;
import services.exception.EntityNotFoundException;
import services.exception.TransactionServiceException;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction) throws TransactionServiceException;

    Transaction getById(String transactionId) throws TransactionServiceException, EntityNotFoundException;

}
