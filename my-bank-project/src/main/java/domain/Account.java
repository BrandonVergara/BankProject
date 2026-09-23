package domain;

import java.math.BigDecimal;

public class Account {

    private Long id;
    private String pin;
    private BigDecimal balance;

    public Account(Long id, String pin, BigDecimal balance) {
        this.id = id;
        this.pin = pin;
        this.balance = balance;
    }

    public Account(String pin) {
        this.id = null;
        this.pin = pin;
        this.balance = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public String getPin() {
        return pin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}