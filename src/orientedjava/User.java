package orientedjava;

import java.util.*;

public class User implements Manageable {
    int id;
    String name;
    String gender;
    int age;
    String phone;

    ArrayList<AccountBook> accountBooks = new ArrayList<>();
    ArrayList<Subscription> subscriptions = new ArrayList<>();

    @Override
    public void read(Scanner scan) {
        id = scan.nextInt();
        name = scan.next();
        gender = scan.next();
        age = scan.nextInt();
        phone = scan.next();
    }

    @Override
    public void print() {
        System.out.printf("[회원 %d] %s (%s, %d세) 전화:%s\n",
                id, name, gender, age, phone);
        if (!subscriptions.isEmpty())
            System.out.println(" └ 구독 서비스 수: " + subscriptions.size());
        if (!accountBooks.isEmpty())
            System.out.println(" └ 등록된 가계부 수: " + accountBooks.size());
    }

    @Override
    public boolean matches(String kwd) {
        return name.contains(kwd) || phone.contains(kwd);
    }
}