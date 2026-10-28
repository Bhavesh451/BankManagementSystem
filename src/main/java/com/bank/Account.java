
package com.bank;

public class Account {
    private long accountNumber;
    private String customerName;
    private String accountType;
    private double balance;

    public Account(String customerName,
                   String accountType,
                   double balance) {
        this.customerName = customerName;
        this.accountType = accountType;
        this.balance = balance;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }
}
