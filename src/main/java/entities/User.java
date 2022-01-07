package entities;

public class User extends BaseEntity<Integer> {

    public static final String TABLE_NAME_SQL = "users";

    public static final String USERNAME_SQL = "username";
    private String username;

    public static final String PASSWORD_HASH_SQL = "passwordHash";
    private String passwordHash;

    public static final String EMAIL_SQL = "email";
    private String email;

    public static final String WALLET_ID_SQL = "walletId";
    private Integer walletId;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getWalletId() {
        return walletId;
    }

    public void setWalletId(Integer walletId) {
        this.walletId = walletId;
    }
}
