package model;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountNumber;
    private String bank;
    private int initialBalance;
    private int currentBalance;
    private List<Transaction> transactionList;
    private String userId;

    public Account(String accountNumber, String bank, int initialBalance) {
        this.accountNumber = accountNumber;
        this.bank = bank;
        this.initialBalance = initialBalance;
        this.currentBalance = initialBalance;
        this.transactionList = new ArrayList<>();
        this.userId = userId;
    }

    public void addTransaction(Transaction tx) {
        this.currentBalance += tx.signedAmount();
        tx.setBalanceAfter(this.currentBalance);
        this.transactionList.add(tx);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account a = (Account) o;
        return java.util.Objects.equals(accountNumber, a.accountNumber)
                && java.util.Objects.equals(bank, a.bank);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(accountNumber, bank);
    }

    @Override
    public String toString() {
        return bank + " / " + accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBank() {
        return bank;
    }

    public int getInitialBalance() {
        return initialBalance;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(int currentBalance) {
        this.currentBalance = currentBalance;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }
}

