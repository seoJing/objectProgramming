package model;

public class SubscriptionService {
    private String serviceName;
    private int amount;
    private String paymentDate;
    private String userId;
    private int subscriptionPeriod;
    private int numberOfUsers;

    public SubscriptionService(String serviceName, int amount, String paymentDate, String userId, int subscriptionPeriod, int numberOfUsers) {
        this.serviceName = serviceName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.userId = userId;
        this.subscriptionPeriod = subscriptionPeriod;
        this.numberOfUsers = numberOfUsers;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getAmount() {
        return amount;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getUserId() {
        return userId;
    }

    public int getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public int getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setSubscriptionPeriod(int subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }

    public void setNumberOfUsers(int numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }
}
