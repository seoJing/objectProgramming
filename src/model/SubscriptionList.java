package model;

import java.util.ArrayList;
import java.util.List;

/**
 * 전체 구독 서비스(상품) 정보를 관리하는 싱글톤
 * DataLoader에서 로드한 상품 정보를 바탕으로 SubscriptionService 목록 관리
 * 정렬, 필터링, 조회 기능 제공
 */
public class SubscriptionList {

    private static SubscriptionList instance;
    private List<SubscriptionService> subscriptions;

    private SubscriptionList() {
        this.subscriptions = new ArrayList<>();
    }

    public static SubscriptionList getInstance() {
        if (instance == null) {
            instance = new SubscriptionList();
        }
        return instance;
    }

    /**
     * 구독 서비스 추가
     */
    public void add(SubscriptionService subscription) {
        if (subscription != null && !subscriptions.contains(subscription)) {
            subscriptions.add(subscription);
        }
    }

    /**
     * 모든 구독 조회
     */
    public List<SubscriptionService> getAll() {
        return new ArrayList<>(subscriptions);
    }

    /**
     * 이름순으로 정렬된 구독 목록 조회
     */
    public List<SubscriptionService> getSortedByName() {
        List<SubscriptionService> sorted = new ArrayList<>(subscriptions);
        sorted.sort((s1, s2) -> s1.getServiceName().compareTo(s2.getServiceName()));
        return sorted;
    }

    /**
     * 금액순으로 정렬된 구독 목록 조회 (높은 금액부터)
     */
    public List<SubscriptionService> getSortedByAmount() {
        List<SubscriptionService> sorted = new ArrayList<>(subscriptions);
        sorted.sort((s1, s2) -> Integer.compare(s2.getAmount(), s1.getAmount()));
        return sorted;
    }

    /**
     * 특정 서비스명으로 검색
     */
    public SubscriptionService findByName(String serviceName) {
        for (SubscriptionService sub : subscriptions) {
            if (sub.getServiceName().equals(serviceName)) {
                return sub;
            }
        }
        return null;
    }

    /**
     * 구독 개수 조회
     */
    public int size() {
        return subscriptions.size();
    }

    /**
     * 구독이 비어있는지 확인
     */
    public boolean isEmpty() {
        return subscriptions.isEmpty();
    }

    /**
     * 모든 구독 초기화
     */
    public void clear() {
        subscriptions.clear();
    }

    @Override
    public String toString() {
        return "SubscriptionList{" +
                "count=" + subscriptions.size() +
                '}';
    }
}
