package model;

import util.UIConstants;

import java.time.LocalDateTime;

public class Transaction {
    private final TransactionType type;    // 입금/출금
    private final int amount;              // 원 단위, 항상 양수 저장
    private final String location;         // 거래 장소
    private final String category;         // 분류(교통, 카페 등)
    private final String memo;             // 메모
    private final LocalDateTime dateTime;  // 거래 시각
    private int balanceAfter;        // 거래 후 잔액
    private Account account;         // 거래 계좌

    public Transaction(TransactionType type, int amount,
                       String location, String category, String memo,
                       LocalDateTime dateTime, int balanceAfter, Account account) {
        this.type = type;
        this.amount = amount;
        this.location = location;
        this.category = category;
        this.memo = memo;
        this.dateTime = dateTime;
        this.balanceAfter = balanceAfter;
        this.account = account;
    }

    public int signedAmount() {
        return (type == TransactionType.INCOME) ? amount : -amount;
    }
    /** Account가 계산한 거래 후 잔액을 기록 */
    public void setBalanceAfter(int balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    public String getDate() {
        if (dateTime != null) {
            // UIConstants에 이미 정의된 포맷 사용 (예: "yyyy.MM.dd HH:mm")
            return dateTime.format(UIConstants.UI_DATEFULL);
        }
        return ""; // 아무 것도 없으면 빈 문자열
    }

    public TransactionType getType()        { return type; }
    public int getAmount()                  { return amount; }
    public String getLocation()             { return location; }
    public String getCategory()             { return category; }
    public String getMemo()                 { return memo; }
    public LocalDateTime getDateTime()      { return dateTime; }
    public int getBalanceAfter()            { return balanceAfter; }

    public String getBank() {
        return (account != null) ? account.getBank() : "";
    }
}
