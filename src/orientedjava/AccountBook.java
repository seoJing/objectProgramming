package orientedjava;

import java.util.*;

public class AccountBook implements Manageable {
    int bookId;
    String ownerName;
    ArrayList<AccountItem> items = new ArrayList<>();

    @Override
    public void read(Scanner scan) {
        bookId = scan.nextInt();
        ownerName = scan.next();
    }

    @Override
    public void print() {
        System.out.printf("[가계부 #%d] 소유자: %s (항목 %d개)\n",
                bookId, ownerName, items.size());
        for (AccountItem item : items)
            item.print();
    }

    @Override
    public boolean matches(String kwd) {
        return ownerName.contains(kwd);
    }

    public void addItem(AccountItem item) {
        items.add(item);
    }
}
