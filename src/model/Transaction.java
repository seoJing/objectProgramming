package model;

public class Transaction {
    private String type;
    private int amount;
    private String location;
    private String date;
    private String category;
    private int balance;
    private String bank;

    public Transaction(String type, int amount, String location, String date, String category, int balance, String bank) {
        this.type = type;
        this.amount = amount;
        this.location = location;
        this.date = date;
        this.category = category;
        this.balance = balance;
        this.bank = bank;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public String getLocation() {
        return location;
    }

    public String getBank() {
        return bank;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
