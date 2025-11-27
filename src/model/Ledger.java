package model;

import java.util.ArrayList;
import java.util.List;

public class Ledger {

    private List<SubscriptionService> subscriptionList;
    private List<Transaction> transactionList;

    public Ledger() {
        this.subscriptionList = new ArrayList<>();
        this.transactionList = new ArrayList<>();
    }

    public List<SubscriptionService> getSubscriptionList() {
        return subscriptionList;
    }

    public void addSubscription(SubscriptionService subscription) {
        subscriptionList.add(subscription);
    }

    public void removeSubscription(SubscriptionService subscription) {
        subscriptionList.remove(subscription);
    }

    private int calculateSubscriptionExpense() {
        int total = 0;
        for (SubscriptionService subscription : subscriptionList) {
            total += subscription.getAmount();
        }
        return total;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void addTransaction(Transaction transaction) {
        transactionList.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactionList.remove(transaction);
    }

    private int calculateTransactionExpense() {
        int total = 0;
        for (Transaction t : transactionList) {
            total += t.getAmount();
        }
        return total;
    }

    public int getMonthlyExpense() {
        int subscriptionExpense = calculateSubscriptionExpense();
        int transactionExpense = calculateTransactionExpense();
        int total = subscriptionExpense + transactionExpense;
        System.out.println("[계산] 월 지출: 구독 ₩" + subscriptionExpense + " + 거래 ₩" + transactionExpense + " = 총 ₩" + total);
        return total;
    }

    // 특정 서비스를 이미 구독 중인지 확인
    public boolean isSubscribed(String serviceName) {
        for (SubscriptionService subscription : subscriptionList) {
            if (subscription.getServiceName().equals(serviceName)) {
                System.out.println("[확인] '" + serviceName + "' - 구독 중: true");
                return true;
            }
        }
        System.out.println("[확인] '" + serviceName + "' - 구독 중: false");
        return false;
    }
}
