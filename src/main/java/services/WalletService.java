package services;

import entities.Wallet;
import services.dto.MoneyDepositWithdrawDetails;
import services.dto.MoneyTransferDetails;
import services.exception.EntityNotFoundException;
import services.exception.WalletServiceException;

public interface WalletService {

    Wallet getById(Long walletId) throws WalletServiceException, EntityNotFoundException;

    String moneyTransferToOtherUser(MoneyTransferDetails moneyTransferDetails) throws WalletServiceException, EntityNotFoundException;

    String deposit(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails) throws WalletServiceException, EntityNotFoundException;

    String withdraw(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails) throws WalletServiceException, EntityNotFoundException;

}
