package services.dto;

public class MoneyDepositWithdrawDetails {

    private Integer walletId;
    private Integer amount;

    public MoneyDepositWithdrawDetails(Integer walletId, Integer amount) {
        this.walletId = walletId;
        this.amount = amount;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
