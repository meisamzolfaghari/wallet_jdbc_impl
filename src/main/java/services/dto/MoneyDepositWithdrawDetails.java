package services.dto;

public class MoneyDepositWithdrawDetails {

    private String username;
    private Integer amount;

    public MoneyDepositWithdrawDetails(String username, Integer amount) {
        this.username = username;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
