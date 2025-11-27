package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SubscriptionService {

    private String serviceName;       // 넷플릭스, 디즈니+
    private int amount;               // 월 요금 (원)
    private String paymentDate;       // 결제일 (yyyy-MM-dd)
    private String userId;            // 구독자 ID
    private int subscriptionPeriod;   // 결제 주기 (1개월/3개월/6개월)
    private int numberOfUsers;        // 공유 인원 수

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public SubscriptionService(String serviceName, int amount, String paymentDate, String userId, int subscriptionPeriod, int numberOfUsers) {
        this.serviceName = serviceName;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.userId = userId;
        this.subscriptionPeriod = subscriptionPeriod;
        this.numberOfUsers = numberOfUsers;
    }

    public String getServiceName() { return serviceName; }
    public int getAmount() { return amount; }
    public String getPaymentDate() { return paymentDate; }
    public String getUserId() { return userId; }
    public int getSubscriptionPeriod() { return subscriptionPeriod; }
    public int getNumberOfUsers() { return numberOfUsers; }

    public void setAmount(int amount) { this.amount = amount; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }
    public void setSubscriptionPeriod(int subscriptionPeriod) { this.subscriptionPeriod = subscriptionPeriod; }
    public void setNumberOfUsers(int numberOfUsers) { this.numberOfUsers = numberOfUsers; }

    // 다음 결제일 계산
    public String getNextPaymentDate() {
        LocalDate date = LocalDate.parse(paymentDate, FORMATTER);
        LocalDate nextDate = date.plusMonths(subscriptionPeriod);
        return nextDate.format(FORMATTER);
    }

    // 1/N 금액 계산 (공유 인원 고려)
    public int getPersonalCost() {
        if (numberOfUsers <= 1) return amount;
        return amount / numberOfUsers;
    }

    public String summary() {
        return serviceName + " | " + amount + "원 | 다음 결제일: " + getNextPaymentDate();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SubscriptionService other = (SubscriptionService) obj;
        return serviceName.equals(other.serviceName) &&
               userId.equals(other.userId) &&
               paymentDate.equals(other.paymentDate);
    }

    @Override
    public int hashCode() {
        return serviceName.hashCode() * 31 + userId.hashCode() * 17 + paymentDate.hashCode();
    }

    @Override
    public String toString() {
        return "SubscriptionService{" +
                "serviceName='" + serviceName + '\'' +
                ", amount=" + amount +
                ", paymentDate='" + paymentDate + '\'' +
                ", nextPaymentDate='" + getNextPaymentDate() + '\'' +
                ", userId='" + userId + '\'' +
                ", period=" + subscriptionPeriod + "개월" +
                ", users=" + numberOfUsers +
                '}';
    }
}
