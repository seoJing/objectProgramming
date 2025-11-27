package util;

import java.util.Map;

import model.SubscriptionService;

public class SubscriptionSavingsUtil {

    /**
     * 서비스 이름에서 베이스 서비스명 추출
     * 예: "넷플릭스_프리미엄" → "넷플릭스"
     */
    public static String extractServiceName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return fullName;
        }
        return fullName.split("[_\\-]")[0];
    }

    /**
     * 서비스 이름에서 등급 추출
     * 예: "넷플릭스_프리미엄" → "프리미엄"
     */
    public static String extractTierName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        String[] parts = fullName.split("[_\\-]");
        if (parts.length > 1) {
            return parts[parts.length - 1];
        }
        return "";
    }

    /**
     * 같은 서비스의 가장 저렴한 등급 옵션 조회
     * @return [등급명, 가격] 또는 null
     */
    public static Object[] getCheapestTierOption(SubscriptionService subscription) {
        String serviceName = extractServiceName(subscription.getServiceName());
        Map<String, DataLoader.ProductInfo> productMap = DataLoader.getProductMap();

        int currentPrice = subscription.getAmount();
        String cheapestTier = null;
        int cheapestPrice = Integer.MAX_VALUE;

        for (DataLoader.ProductInfo product : productMap.values()) {
            // 같은 서비스명의 다른 상품 찾기
            if (product.name.startsWith(serviceName + "_") || product.name.startsWith(serviceName + "-")) {
                // 현재 가격보다 저렴한 것들 중에서 가장 저렴한 것 선택
                if (product.price < currentPrice && product.price < cheapestPrice) {
                    cheapestPrice = product.price;
                    cheapestTier = extractTierName(product.name);
                }
            }
        }

        if (cheapestTier != null && cheapestPrice < Integer.MAX_VALUE) {
            return new Object[] { cheapestTier, cheapestPrice };
        }
        return null;
    }

    /**
     * 공유 계정으로 계산된 실제 구독료 계산
     * @return 실제 지불할 금액 (공유자 수로 나눈 금액)
     */
    public static int calculateActualPrice(SubscriptionService subscription) {
        int numberOfUsers = subscription.getNumberOfUsers();
        if (numberOfUsers <= 1) {
            return subscription.getAmount();
        }
        return subscription.getAmount() / numberOfUsers;
    }

    /**
     * 절약액 계산
     * @return [절약액, 메시지] 또는 null
     */
    public static Object[] calculateSavings(SubscriptionService subscription) {
        // 공유 중이면 절약액 표시 안 함
        if (subscription.getNumberOfUsers() > 1) {
            return null;
        }

        int currentPrice = subscription.getAmount();

        // 1. 등급이 있는 경우 - 가장 저렴한 등급으로 변경 시 절약액
        Object[] cheapestOption = getCheapestTierOption(subscription);
        if (cheapestOption != null) {
            String tierName = (String) cheapestOption[0];
            int cheapestPrice = (Integer) cheapestOption[1];
            int savingsPerMonth = currentPrice - cheapestPrice;
            int savingsPerYear = savingsPerMonth * 12;
            String message = tierName + "로 변경 시 연간 " + String.format("%,d", savingsPerYear) + "원 절약";
            return new Object[] { savingsPerYear, message };
        }

        // 2. 등급이 없는 경우 - 4명 가족 공유 시 절약액
        int familySavingsPerYear = currentPrice * 9; // 월 요금 × 12 × (3/4)
        String familyMessage = "4명 가족 공유 시 연간 " + String.format("%,d", familySavingsPerYear) + "원 절약";
        return new Object[] { familySavingsPerYear, familyMessage };
    }
}
