package util;

import model.Account;
import model.SubscriptionService;
import model.User;
import model.Transaction;
import java.time.LocalDate;

public class SessionManager {
    private static SessionManager instance;

    private User currentUser;
    private SubscriptionService selectedSubscription;
    private Account selectedAccount;
    private Transaction selectedTransaction;
    private LocalDate selectedDate;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public void logout() {
        currentUser = null;
        selectedSubscription = null;
        selectedAccount = null;
    }

    public void setSelectedSubscription(SubscriptionService subscription) {
        this.selectedSubscription = subscription;
    }

    public SubscriptionService getSelectedSubscription() {
        return selectedSubscription;
    }

    public void setSelectedAccount(Account account) {
        this.selectedAccount = account;
    }

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public void setSelectedTransaction(Transaction transaction) {
        this.selectedTransaction = transaction;
    }

    public Transaction getSelectedTransaction() {
        return selectedTransaction;
    }

    public void setSelectedDate(LocalDate d) { this.selectedDate = d; }
    public LocalDate getSelectedDate() { return selectedDate; }
}
