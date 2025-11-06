package model;

import java.util.ArrayList;
import java.util.List;

public class Ledger {
    private List<SubscriptionService> subscriptionList;

    public Ledger() {
        this.subscriptionList = new ArrayList<>();
    }

    public int getMonthlyExpense() {
        return calculateMonthlyExpense();
    }

    private int calculateMonthlyExpense() {
        int total = 0;
        for (SubscriptionService subscription : subscriptionList) {
            total += subscription.getAmount();
        }
        return total;
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
}
