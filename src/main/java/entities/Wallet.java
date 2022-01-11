package entities;

public class Wallet extends BaseEntity<Long> {

    public static final String TABLE_NAME_SQL = "wallets";

    public static final String BALANCE_SQL = "balance";
    private Integer balance;

    public Wallet() {
        this.balance = 0;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

}
