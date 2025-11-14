package model;

import util.UIConstants;
import java.time.LocalDateTime;

public class Transaction {

    private final TransactionType type;    // 입금 / 출금
    private final int amount;              // 항상 양수
    private final String location;         // 거래 장소
    private final String category;         // 카테고리(식비/쇼핑 등)
    private final String memo;             // 메모
    private final LocalDateTime dateTime;  // 거래 시각

    private int balanceAfter;              // 거래 후 잔액
    private String bank = "";              // 은행명

    public Transaction(TransactionType type, int amount,
                       String location, String category, String memo,
                       LocalDateTime dateTime, int balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.location = location;
        this.category = category;
        this.memo = memo;
        this.dateTime = dateTime;
        this.balanceAfter = balanceAfter;
    }

    /** 수입이면 양수, 지출이면 음수 반환 */
    public int signedAmount() {
        return (type == TransactionType.INCOME) ? amount : -amount;
    }

    /** 거래 후 잔액 기록 */
    public void setBalanceAfter(int balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    /** 은행명 설정 (필요 시) */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /** UI 날짜 출력 */
    public String getDate() {
        if (dateTime != null)
            return dateTime.format(UIConstants.UI_DATEFULL);
        return "";
    }

    public TransactionType getType()        { return type; }
    public int getAmount()                  { return amount; }
    public String getLocation()             { return location; }
    public String getCategory()             { return category; }
    public String getMemo()                 { return memo; }
    public LocalDateTime getDateTime()      { return dateTime; }
    public int getBalanceAfter()            { return balanceAfter; }
    public String getBank()                 { return bank; }

    /** 가계부에서 항목 출력용 */
    public String summary() {
        return category + " | " + amount + "원 | " + getDate();
    }

    @Override
    public String toString() {
        return "[" + getDate() + "] "
                + category + " - "
                + amount + "원"
                + (memo != null && !memo.isEmpty() ? (" (" + memo + ")") : "");
    }
}