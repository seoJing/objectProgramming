package model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String password;
    private String name;
    private String gender;
    private int age;
    private String occupation;
    private String residence;
    private String phoneNumber;
    private boolean isAdmin;

    private List<Account> accountList;
    private Ledger ledger;

    public User(String id, String password, String name, String gender, int age,
                String occupation, String residence, String phoneNumber, boolean isAdmin) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.occupation = occupation;
        this.residence = residence;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;

        this.accountList = new ArrayList<>();
        this.ledger = new Ledger();
    }

    // ==============================
    // Getter
    // ==============================

    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    public String getOccupation() { return occupation; }
    public String getResidence() { return residence; }
    public String getPhoneNumber() { return phoneNumber; }
    public boolean isAdmin() { return isAdmin; }
    public List<Account> getAccountList() { return accountList; }
    public Ledger getLedger() { return ledger; }

    // ==============================
    // Setter
    // ==============================

    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAge(int age) { this.age = age; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public void setResidence(String residence) { this.residence = residence; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    // ==============================
    // 기능 1) 계좌 추가/삭제/조회
    // ==============================

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

    // ==============================
    // 기능 2) 비밀번호 변경
    // ==============================

    public boolean changePassword(String oldPw, String newPw) {
        if (this.password.equals(oldPw)) {
            this.password = newPw;
            return true;
        }
        return false;
    }

    // ==============================
    // 기능 3) 사용자 정보 업데이트
    // ==============================

    public void updateProfile(String name, String gender, int age,
                              String occupation, String residence, String phoneNumber) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.occupation = occupation;
        this.residence = residence;
        this.phoneNumber = phoneNumber;
    }

    // ==============================
    // 기능 4) 출력 포맷
    // ==============================

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
                ", admin=" + isAdmin +
                '}';
    }
}