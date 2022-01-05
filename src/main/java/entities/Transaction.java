package entities;

import java.util.Date;

public class Transaction extends BaseEntity<String> {

    public static final String TABLE_NAME_SQL = "transactions";

    public static final String TRANSACTION_DATE_SQL = "name";
    private Date transactionDate;

    public static final String STATUS_SQL = "status";
    private String status;

    public static final String TYPE_SQL = "type";
    private String type;

    public static final String AMOUNT_SQL = "amount";
    private Integer amount;

    public static final String WALLET_ID_SQL = "walletId";
    private final Integer walletId;

    public Transaction(Integer walletId) {
        this.status = TransactionStatus.NEW.name();
        this.walletId = walletId;
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

    public Integer getWalletId() {
        return walletId;
    }

}
