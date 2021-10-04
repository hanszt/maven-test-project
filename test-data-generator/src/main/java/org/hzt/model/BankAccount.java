package org.hzt.model;

import java.math.BigDecimal;

public class BankAccount {

    private final String accountNumber;
    private final Customer customer;
    private BigDecimal balance;

    public BankAccount(String accountNumber, Customer customer, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = balance;
    }

    public BankAccount(String accountNumber, Customer customer) {
        this(accountNumber, customer, BigDecimal.ZERO);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal updateBalance(BigDecimal balance) {
        this.balance = balance;
        return this.balance;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", customer=" + customer +
                ", balance=" + balance +
                '}';
    }
}
