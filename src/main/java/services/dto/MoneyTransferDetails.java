package services.dto;

public class MoneyTransferDetails {

    private Integer senderWalletId;
    private Integer receiverWalletId;

    private Integer amount;

    public MoneyTransferDetails() {
    }

    public MoneyTransferDetails(Integer receiverWalletId, Integer senderWalletId, Integer amount) {
        this.receiverWalletId = receiverWalletId;
        this.senderWalletId = senderWalletId;
        this.amount = amount;
    }

    public Integer getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setReceiverWalletId(Integer receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }

    public Integer getSenderWalletId() {
        return senderWalletId;
    }

    public void setSenderWalletId(Integer senderWalletId) {
        this.senderWalletId = senderWalletId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
