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
    public static final String MY_PAGE = "MY_PAGE";
    public static final String TRANSACTION = "TRANSACTION";
    public static final String SUBSCRIPTION_DETAIL = "SUBSCRIPTION_DETAIL";

    // Admin screens
    public static final String ADMIN_LOGIN = "ADMIN_LOGIN";
    public static final String ADMIN_DASHBOARD = "ADMIN_DASHBOARD";
    public static final String ADMIN_SUBSCRIPTION_MACHINE = "ADMIN_SUBSCRIPTION_MACHINE";
    public static final String ADMIN_SUBSCRIPTION_MANAGE = "ADMIN_SUBSCRIPTION_MANAGE";
    public static final String ADMIN_STATISTICS = "ADMIN_STATISTICS";
}
