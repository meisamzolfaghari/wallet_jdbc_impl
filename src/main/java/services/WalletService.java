package services;

import entities.Wallet;
import services.dto.MoneyDepositWithdrawDetails;
import services.dto.MoneyTransferDetails;
import services.exception.UserNotFoundException;
import services.exception.WalletServiceException;

public interface WalletService {

    Wallet getById(Integer walletId) throws WalletServiceException;

    String moneyTransferToOtherUser(MoneyTransferDetails moneyTransferDetails) throws WalletServiceException, UserNotFoundException;

    String deposit(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails) throws WalletServiceException, UserNotFoundException;

    String withdraw(MoneyDepositWithdrawDetails moneyDepositWithdrawDetails) throws WalletServiceException, UserNotFoundException;

}
