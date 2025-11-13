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

        public void addTransaction(Transaction transaction) {
            this.transactionList.add(transaction);
    }
}
