package services.dto;

public class MoneyTransferDetails {

    private String senderUsername;
    private String receiverUsername;

    private Integer amount;

    public MoneyTransferDetails() {
    }

    public MoneyTransferDetails(String senderUsername, String receiverUsername, Integer amount) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.amount = amount;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
