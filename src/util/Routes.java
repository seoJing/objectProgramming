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
    public static final String TRANSACTION_DETAIL = "TRANSACTION_DETAIL";
    public static final String SUBSCRIPTION_DETAIL = "SUBSCRIPTION_DETAIL";
    public static final String ALERT = "ALERT";
    public static final String STORE = "STORE";
    public static final String SETTING = "SETTING";
    public static final String GROUP = "GROUP";
    public static final String STORE_DETAIL = "STORE_DETAIL";


    // Admin screens
    public static final String ADMIN_MAIN = "ADMIN_MAIN";
    public static final String ADMIN_SUBSCRIPTION_MACHINE = "ADMIN_SUBSCRIPTION_MACHINE";
    public static final String ADMIN_SUBSCRIPTION_MANAGE = "ADMIN_SUBSCRIPTION_MANAGE";
    public static final String ADMIN_STATISTICS = "ADMIN_STATISTICS";
}
