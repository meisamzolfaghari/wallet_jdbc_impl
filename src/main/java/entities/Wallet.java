package entities;

public class Wallet extends BaseEntity<Integer> {

    public static final String TABLE_NAME_SQL = "wallets";

    public static final String BALANCE_SQL = "balance";
    private Integer balance;

    public static final String USER_ID_SQL = "userId";
    private final Integer userId;

    public Wallet(Integer userId) {
        this.balance = 0;
        this.userId = userId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getUserId() {
        return userId;
    }

}
