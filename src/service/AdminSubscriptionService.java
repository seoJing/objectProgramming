package service;

import java.util.ArrayList;
import java.util.List;

import model.SubscriptionService;

public class AdminSubscriptionService {

    private static AdminSubscriptionService instance;
    private List<SubscriptionService> list;

    private AdminSubscriptionService() {
        list = new ArrayList<>();

        // ★ Mock 데이터 (관리자 화면용)
        list.add(new SubscriptionService("Netflix", 17000, "2024-12-05", "admin", 1, 1));
        list.add(new SubscriptionService("Disney+", 9900, "2024-12-20", "admin", 1, 2));
        list.add(new SubscriptionService("YouTube Premium", 12500, "2024-12-02", "admin", 1, 3));
    }

    public static AdminSubscriptionService getInstance() {
        if (instance == null) instance = new AdminSubscriptionService();
        return instance;
    }

    public List<SubscriptionService> getAll() {
        return list;
    }

    public SubscriptionService findByName(String name) {
        return list.stream().filter(s -> s.getServiceName().equals(name)).findFirst().orElse(null);
    }

    public void updatePrice(String name, int newPrice) {
        SubscriptionService s = findByName(name);
        if (s != null) s.setAmount(newPrice);
    }

    public void updateNextPayment(String name) {
        SubscriptionService s = findByName(name);
        if (s != null) {
            String next = s.getNextPaymentDate();
            s.setPaymentDate(next); // 다음 결제일로 갱신
        }
    }
}
