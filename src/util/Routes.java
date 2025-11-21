package util;

/**
 * Screen route constants for navigation.
 * Use these constants instead of hardcoded strings to prevent typos and maintain consistency.
 */
public final class Routes {

    private Routes() {
        // Prevent instantiation
    }

    // Main screens
    public static final String LOGIN = "LOGIN";
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

    // User screens
    public static final String MAIN = "MAIN";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String SUBSCRIPTION = "SUBSCRIPTION";
    public static final String TRANSACTION = "TRANSACTION";
    public static final String ALL_TRANSACTIONS = "ALL_TRANSACTIONS";
    public static final String TRANSACTION_DETAIL = "TRANSACTION_DETAIL";
    public static final String SUBSCRIPTION_DETAIL = "SUBSCRIPTION_DETAIL";
    public static final String ALERT = "ALERT";
    public static final String STORE = "STORE";
    public static final String SETTING = "SETTING";
    public static final String GROUP = "GROUP";
    public static final String GROUP_LIST = "GROUP_LIST";
    public static final String GROUP_DETAIL = "GROUP_DETAIL";
    public static final String STORE_DETAIL = "STORE_DETAIL";

    // Admin screens (최종 통합)
    public static final String ADMIN_MAIN = "ADMIN_MAIN";
    public static final String ADMIN_USERS = "ADMIN_USERS"; // 사용자 조회
    public static final String ADMIN_SUB_MANAGE = "ADMIN_SUB_MANAGE"; // 구독 관리
    public static final String ADMIN_TRANSACTION_MANAGE = "ADMIN_TRANSACTION_MANAGE"; // 거래 관리(팀원 코드)
    public static final String ADMIN_STATISTICS = "ADMIN_STATISTICS"; // 통계
}
