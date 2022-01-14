package controllers.transaction.dto;

import java.util.Date;

public class TransactionDTO {

    private String id;

    private Date date;

    private String status;

    private String type;

    private String senderUsername;

    private String receiverUsername;

    public TransactionDTO(String id, Date date, String status, String type, String senderUsername, String receiverUsername) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.type = type;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }
}
