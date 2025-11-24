package util;

import model.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DataLoader {

    private static final String DATA_PATH = "src/resources/data/";

    public static void loadAll() {
        loadUsers();
        loadAccounts();
        loadSubscriptions();
        loadTransactions();
    }

    private static File getFile(String fileName) {
        File file = new File(DATA_PATH + fileName);
        if (!file.exists()) file = new File(fileName);
        return file;
    }

    // 1. 유저 로딩
    private static void loadUsers() {
        File file = getFile("users.txt");
        if (!file.exists()) return;

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String id = scan.next();
                String pw = scan.next();
                String name = scan.next();
                String gender = scan.next();
                int age = scan.nextInt();
                String job = scan.next();
                String city = scan.next();
                String phone = scan.next();
                boolean isAdmin = scan.nextBoolean();

                if (isAdmin || id.equals("admin")) continue;

                if (UserList.getInstance().findById(id) == null) {
                    User u = new User(id, pw, name, gender, age, job, city, phone, isAdmin);
                    UserList.getInstance().add(u);
                }
            }
            System.out.println("✅ 유저 로딩 완료 (" + UserList.getInstance().size() + "명, 관리자 제외)");
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 2. 계좌 로딩
    private static void loadAccounts() {
        File file = getFile("accounts.txt");
        if (!file.exists()) return;
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String userId = scan.next();
                String bank = scan.next();
                String accNum = scan.next();
                int balance = scan.nextInt();

                User u = UserList.getInstance().findById(userId);
                if (u != null) {
                    u.addAccount(new Account(accNum, bank, balance, userId));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 3. 구독 로딩
    private static void loadSubscriptions() {
        File file = getFile("subscription.txt");
        if (!file.exists()) return;
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String userId = scan.next();
                SubscriptionService sub = new SubscriptionService(
                        scan.next(), scan.nextInt(), scan.next(), userId, scan.nextInt(), scan.nextInt()
                );
                User u = UserList.getInstance().findById(userId);
                if (u != null) u.getLedger().addSubscription(sub);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 4. 거래내역 로딩
    private static void loadTransactions() {
        File file = getFile("transactions.txt");
        if (!file.exists()) return;

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String accNum = scan.next();

                // ★ [수정] 0을 만나면 '건너뛰기(continue)'가 아니라 '종료(break)'합니다.
                if (accNum.equals("0")) break;

                Account targetAccount = findAccountByNumber(accNum);
                if (targetAccount == null) {
                    scan.nextLine(); // 계좌 못 찾으면 줄 건너뛰기
                    continue;
                }

                String typeStr = scan.next();
                TransactionType type = typeStr.equals("입금") ? TransactionType.INCOME : TransactionType.EXPENSE;
                int amount = scan.nextInt();
                String loc = scan.next();
                String cat = scan.next();

                // 날짜/메모 스마트 판별
                String temp = scan.next();
                String memo = "";
                String dateStr = "";

                if (temp.startsWith("20") && temp.contains("T")) {
                    dateStr = temp;
                    memo = "";
                } else {
                    memo = temp;
                    if (scan.hasNext()) dateStr = scan.next();
                }

                LocalDateTime dt = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                Transaction tx = new Transaction(type, amount, loc, cat, memo, dt, 0, targetAccount);
                targetAccount.addTransaction(tx);
            }
        } catch (Exception e) {
        }
    }

    private static Account findAccountByNumber(String accNum) {
        for (User u : UserList.getInstance().getAll()) {
            for (Account acc : u.getAccountList()) {
                if (acc.getAccountNumber().equals(accNum)) return acc;
            }
        }
        return null;
    }
}
