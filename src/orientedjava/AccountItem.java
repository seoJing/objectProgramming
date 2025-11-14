package orientedjava;

import java.util.*;
import java.time.*;

public class AccountItem implements Manageable {
    int itemId;
    LocalDate date;
    String category;
    String type;  // 수입/지출
    double amount;
    String memo;

    @Override
    public void read(Scanner scan) {
        itemId = scan.nextInt();
        date = LocalDate.parse(scan.next());
        category = scan.next();
        type = scan.next();
        amount = scan.nextDouble();
        memo = scan.next();
    }

    @Override
    public void print() {
        System.out.printf("   [%d] %s | %s | %s %.0f원 | 메모: %s\n",
                itemId, date, category, type, amount, memo);
    }

    @Override
    public boolean matches(String kwd) {
        return category.contains(kwd) || memo.contains(kwd);
    }
}