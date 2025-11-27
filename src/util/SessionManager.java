package util;

import java.time.LocalDate;

import model.Account;
import model.SubscriptionService;
import model.Transaction;
import model.User;

public class SessionManager {

    private static SessionManager instance;

    private User currentUser;
    private SubscriptionService selectedSubscription;
    private Account selectedAccount;
    private Transaction selectedTransaction;
    private LocalDate selectedDate;

    private boolean fromAllTransactions = false;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
        this.selectedSubscription = null;
        this.selectedAccount = null;
        this.fromAllTransactions = false;
    }

    public void logout() {
        this.currentUser = null;
        this.selectedSubscription = null;
        this.selectedAccount = null;
        this.fromAllTransactions = false;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public SubscriptionService getSelectedSubscription() {
        return selectedSubscription;
    }


    public void setSelectedAccount(Account account) {
        this.selectedAccount = account;
    }

    public void setSelectedSubscription(SubscriptionService subscription) {
        this.selectedSubscription = subscription;
    }

    public void setSelectedTransaction(Transaction transaction) {
        this.selectedTransaction = transaction;
    }

    public Transaction getSelectedTransaction() {
        return selectedTransaction;
    }

    public void setSelectedDate(LocalDate d) { this.selectedDate = d; }
    public LocalDate getSelectedDate() { return selectedDate; }

    public boolean isFromAllTransactions() {
        return fromAllTransactions;
    }
    public void setFromAllTransactions(boolean fromAllTransactions) {
        this.fromAllTransactions = fromAllTransactions;
    }
}
