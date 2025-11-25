package util;

import model.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DataLoader {

    private static final String DATA_PATH = "src/resources/data/";

    public static void loadAll() {
        System.out.println("[DataLoader] 데이터 로딩 시작...");
        loadUsers();
        loadAccounts();
        loadSubscriptions();
        loadTransactions();
        System.out.println("[DataLoader] 데이터 로딩 완료.");
    }

    private static File getFile(String fileName) {
        File file = new File(DATA_PATH + fileName);
        if (!file.exists()) file = new File(fileName);
        return file;
    }

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

                // 관리자는 로딩 제외 (요구사항 반영)
                if (isAdmin || id.equals("admin")) continue;

                if (UserList.getInstance().findById(id) == null) {
                    User u = new User(id, pw, name, gender, age, job, city, phone, isAdmin);
                    UserList.getInstance().add(u);
                }
            }
            System.out.println("✅ 유저 로딩 완료: " + UserList.getInstance().size() + "명");
        } catch (Exception e) { e.printStackTrace(); }
    }

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
            System.out.println("✅ 계좌 로딩 완료");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void loadSubscriptions() {
        File file = getFile("subscriptions.txt");
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
            System.out.println("✅ 구독 로딩 완료");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void loadTransactions() {
        File file = getFile("transactions.txt");
        if (!file.exists()) return;

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String accNum = scan.next();
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

                // ★ [수정 핵심] 날짜인지 아닌지 직접 확인하는 강력한 로직
                String temp = scan.next();
                String memo = "";
                String dateStr = "";

                try {
                    // 1. 일단 날짜라고 가정하고 파싱 시도
                    LocalDate.parse(temp, DateTimeFormatter.ISO_DATE);
                    // 2. 성공하면 날짜임 (메모 없음)
                    dateStr = temp;
                } catch (Exception e) {
                    // 3. 실패하면 메모임 -> 그 다음 토큰이 날짜
                    memo = temp;
                    dateStr = scan.next();
                }

                LocalDateTime dt = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE).atStartOfDay();
                Transaction tx = new Transaction(type, amount, loc, cat, memo, dt, 0, targetAccount);

                targetAccount.addTransaction(tx);
            }
            System.out.println("✅ 거래내역 로딩 완료");
        } catch (Exception e) { e.printStackTrace(); }
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
