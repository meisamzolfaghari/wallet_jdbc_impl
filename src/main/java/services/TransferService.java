package services;

import entities.Transaction;
import services.exception.TransferServiceException;

public interface TransferService {

    void transferMoney(Transaction transaction) throws TransferServiceException;

    void depositMoney(Transaction transaction) throws TransferServiceException;

    void withdrawMoney(Transaction transaction) throws TransferServiceException;

}
