package entities;

import java.util.Date;

public class Transaction extends BaseEntity<String> {

    public static final String TABLE_NAME_SQL = "transactions";

    public static final String TRANSACTION_DATE_SQL = "transactionDate";
    private Date transactionDate;

    public static final String STATUS_SQL = "status";
    private String status;

    public static final String TYPE_SQL = "type";
    private String type;

    public static final String AMOUNT_SQL = "amount";
    private Integer amount;

    public static final String SENDER_WALLET_ID_SQL = "senderWalletId";
    private Integer senderWalletId;

    public static final String RECEIVER_WALLET_ID_SQL = "receiverWalletId";
    private Integer receiverWalletId;

    public Transaction(String id, Date transactionDate, String status, String type,
                       Integer amount, Integer senderWalletId, Integer receiverWalletId) {
        this.id = id;
        this.transactionDate = transactionDate;
        this.status = status;
        this.type = type;
        this.amount = amount;
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getSenderWalletId() {
        return senderWalletId;
    }

    public void setSenderWalletId(Integer senderWalletId) {
        this.senderWalletId = senderWalletId;
    }

    public Integer getReceiverWalletId() {
        return receiverWalletId;
    }

    public void setReceiverWalletId(Integer receiverWalletId) {
        this.receiverWalletId = receiverWalletId;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME_SQL;
    }
}
