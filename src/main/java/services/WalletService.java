package services;

import entities.Wallet;
import services.dto.MoneyDepositWithdrawDetails;
import services.dto.MoneyTransferDetails;
import services.exception.WalletServiceException;

public interface WalletService {

    Wallet getById(Integer walletId) throws WalletServiceException;

    String moneyTransferToOtherUser(MoneyTransferDetails moneyTransferDetails) throws WalletServiceException;

    String deposit(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails) throws WalletServiceException;

    String withdraw(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails) throws WalletServiceException;

}
