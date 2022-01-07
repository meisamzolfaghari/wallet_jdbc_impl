package entities.dto;

public class MoneyTransferDetails {

    private String receiverUsername;
    private String senderUsername;

    private Integer amount;

    public MoneyTransferDetails() {
    }

    public MoneyTransferDetails(String receiverUsername, String senderUsername, Integer amount) {
        this.receiverUsername = receiverUsername;
        this.senderUsername = senderUsername;
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
