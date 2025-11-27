package model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String password;      // 해시된 비밀번호 저장
    private String name;
    private String gender;
    private int age;
    private String occupation;
    private String residence;
    private String phoneNumber;
    private boolean admin;

    private final List<Account> accountList;
    private final Ledger ledger;

    public User(
            String id,
            String password,
            String name,
            String gender,
            int age,
            String occupation,
            String residence,
            String phoneNumber,
            boolean admin
    ) {
        this.id = id;
        this.password = password;
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

    public String getId() { return id; }
    public String getPasswordHash() { return password; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public String getOccupation() { return occupation; }
    public String getResidence() { return residence; }
    public String getPhoneNumber() { return phoneNumber; }
    public boolean isAdmin() { return admin; }
    public List<Account> getAccountList() {
        return accountList;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAge(int age) { this.age = age; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public void setResidence(String residence) { this.residence = residence; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void addAccount(Account account) {
        accountList.add(account);
    }

    public void removeAccount(Account account) {
        accountList.remove(account);
    }

    public Account getAccountByNumber(String accountNumber) {
        for (Account acc : accountList) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                System.out.println("[검색] 계좌 찾음: " + accountNumber + " (" + acc.getBank() + ")");
                return acc;
            }
        }
        System.out.println("[검색] 계좌 '" + accountNumber + "' - 찾을 수 없음");
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", occupation='" + occupation + '\'' +
                ", residence='" + residence + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", admin=" + admin +
                '}';
    }
}
