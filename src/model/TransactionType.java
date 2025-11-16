package model;

public enum TransactionType {
    INCOME("입금"),
    EXPENSE("출금");

    private final String label;
    TransactionType(String label) { this.label = label; }
    @Override public String toString() { return label; }
}
