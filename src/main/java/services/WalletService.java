package services;

import entities.Wallet;
import entities.dto.MoneyDepositDetails;
import entities.dto.MoneyTransferDetails;
import services.exception.WalletServiceException;

public interface WalletService {

    Wallet getWallet(Integer walletId) throws WalletServiceException;

    String moneyTransferToOtherUser(MoneyTransferDetails moneyTransferDetails) throws WalletServiceException;

//    String deposit(MoneyDepositDetails moneyDepositDetails) throws WalletServiceException;

}
