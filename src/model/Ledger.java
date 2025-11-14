package model;

import java.util.ArrayList;
import java.util.List;

public class Ledger {

    // 구독 서비스 리스트
    private List<SubscriptionService> subscriptionList;

    // 가계부 일반 항목 리스트
    private List<Transaction> transactionList;

    public Ledger() {
        this.subscriptionList = new ArrayList<>();
        this.transactionList = new ArrayList<>();
    }

    // ==============================
    // 구독 서비스 관련 메서드
    // ==============================

    public List<SubscriptionService> getSubscriptionList() {
        return subscriptionList;
    }

    public void addSubscription(SubscriptionService subscription) {
        subscriptionList.add(subscription);
    }

    public void removeSubscription(SubscriptionService subscription) {
        subscriptionList.remove(subscription);
    }

    // 한 달 구독 서비스 total 비용
    private int calculateSubscriptionExpense() {
        int total = 0;
        for (SubscriptionService subscription : subscriptionList) {
            total += subscription.getAmount();
        }
        return total;
    }

    // ==============================
    // 가계부 일반 항목(Transaction) 관련 메서드
    // ==============================

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactionList.remove(transaction);
    }

    // 일반 항목 비용 총합
    private int calculateTransactionExpense() {
        int total = 0;
        for (Transaction t : transactionList) {
            total += t.getAmount();
        }
        return total;
    }

    // ==============================
    // 전체 월 지출 합산
    // ==============================

    public int getMonthlyExpense() {
        return calculateSubscriptionExpense() + calculateTransactionExpense();
    }
}
