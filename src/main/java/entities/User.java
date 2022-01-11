package entities;

import java.io.Serializable;

public class User extends BaseEntity<Long> implements Serializable {

    public static final String TABLE_NAME_SQL = "users";

    public static final String USERNAME_SQL = "username";
    private String username;

    public static final String PASSWORD_HASH_SQL = "passwordHash";
    private String passwordHash;

    public static final String EMAIL_SQL = "email";
    private String email;

    public static final String WALLET_ID_SQL = "walletId";
    private Long walletId;

    public User() {
    }

    public User(String username, String passwordHash, String email) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

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

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }
}
