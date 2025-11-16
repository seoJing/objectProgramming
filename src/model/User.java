package model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String id;
    private final String passwordHash;      // 해시된 비밀번호 
    private String name;
    private String gender;
    private int age;
    private String occupation;
    private String residence;
    private String phoneNumber;
    private final boolean admin;

    private final List<Account> accountList;
    private final Ledger ledger;

    public User(
            String id,
            String passwordHash,
            String name,
            String gender,
            int age,
            String occupation,
            String residence,
            String phoneNumber,
            boolean admin
    ) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.occupation = occupation;
        this.residence = residence;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
        this.accountList = new ArrayList<>();
        this.ledger = new Ledger();
    }

    // ===================== Getter =====================

    public String getId() {
        return id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getResidence() {
        return residence;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isAdmin() {
        return admin;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public Ledger getLedger() {
        return ledger;
    }

    // ===================== Setter =====================

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // ===================== Business Logic =====================

    public void addAccount(Account account) {
        accountList.add(account);
    }

    public void removeAccount(Account account) {
        accountList.remove(account);
    }

    public Account getAccountByNumber(String accountNumber) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
}
