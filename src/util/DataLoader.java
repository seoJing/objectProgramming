package util;

import model.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataLoader {

    private static final String DATA_PATH = "src/resources/data/";

    // 상품 정보를 임시로 저장할 맵 (ID -> ProductInfo)
    private static final Map<String, ProductInfo> productMap = new HashMap<>();

    // 내부적으로만 사용할 상품 정보 클래스
    public static class ProductInfo {
        public String category;
        public String name;
        public int price;

        public ProductInfo(String category, String name, int price) {
            this.category = category;
            this.name = name;
            this.price = price;
        }
    }

    // 상품 정보 맵 접근 메서드
    public static Map<String, ProductInfo> getProductMap() {
        return productMap;
    }

    public static void loadAll() {
        loadUsers();
        loadAccounts();
        loadProducts();      // 1. 상품 정보 먼저 로딩
        loadSubscriptions(); // 2. 구독 정보 로딩 (상품 ID 참조)
        loadTransactions();
    }

    private static File getFile(String fileName) {
        File file = new File(DATA_PATH + fileName);
        if (!file.exists()) file = new File(fileName);
        return file;
    }

    // ========================================================
    // 1. 상품 정보 로딩 (products.txt)
    // 포맷: 상품ID 카테고리 상품명 가격
    // ========================================================
    private static void loadProducts() {
        File file = getFile("products.txt");
        if (!file.exists()) return;

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String pid = scan.next();
                String category = scan.next();
                String name = scan.next();
                int price = scan.nextInt();

                productMap.put(pid, new ProductInfo(category, name, price));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================================================
    // 2. 구독 정보 로딩 (subscription.txt)
    // 포맷: 유저ID 상품ID 결제일 주기 인원수
    // ========================================================
    private static void loadSubscriptions() {
        File file = getFile("subscription.txt");
        if (!file.exists()) return;

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String userId = scan.next();
                String productId = scan.next();
                String paymentDate = scan.next();
                int period = scan.nextInt();
                int users = scan.nextInt();

                // 상품 ID로 미리 로드해둔 정보 조회
                ProductInfo product = productMap.get(productId);

                if (product != null) {
                    // 상품 정보와 구독 정보를 합쳐서 객체 생성
                    SubscriptionService sub = new SubscriptionService(
                            product.name,
                            product.price,
                            paymentDate,
                            userId,
                            period,
                            users
                    );

                    // 유저에게 구독 추가
                    User u = UserList.getInstance().findById(userId);
                    if (u != null) {
                        u.getLedger().addSubscription(sub);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                if (isAdmin || id.equals("admin")) continue;

                if (UserList.getInstance().findById(id) == null) {
                    User u = new User(id, pw, name, gender, age, job, city, phone, isAdmin);
                    UserList.getInstance().add(u);
                }
            }
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
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void loadTransactions() {
        File file = getFile("transactions.txt");
        if (!file.exists()) return;

        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String accNum = scan.next();
                if (accNum.equals("0")) break;

                Account targetAccount = findAccountByNumber(accNum);
                if (targetAccount == null) {
                    scan.nextLine();
                    continue;
                }

                String typeStr = scan.next();
                TransactionType type = typeStr.equals("입금") ? TransactionType.INCOME : TransactionType.EXPENSE;
                int amount = scan.nextInt();
                String loc = scan.next();
                String cat = scan.next();

                String temp = scan.next();
                String memo = "";
                String dateStr = "";

                if (temp.startsWith("20") && temp.contains("T")) {
                    dateStr = temp;
                } else {
                    memo = temp;
                    if (scan.hasNext()) dateStr = scan.next();
                }

                LocalDateTime dt = LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                Transaction tx = new Transaction(type, amount, loc, cat, memo, dt, 0, targetAccount);
                targetAccount.addTransaction(tx);
            }
        } catch (Exception e) { }
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
