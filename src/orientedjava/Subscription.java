package orientedjava;

import java.util.*;
import java.time.*;

public class Subscription implements Manageable {
    String serviceName;
    String category;
    String plan;
    LocalDate startDate;
    LocalDate endDate;
    double monthlyFee;

    @Override
    public void read(Scanner scan) {
        serviceName = scan.next();
        category = scan.next();
        plan = scan.next();
        startDate = LocalDate.parse(scan.next());
        endDate = LocalDate.parse(scan.next());
        monthlyFee = scan.nextDouble();
    }

    @Override
    public void print() {
        System.out.printf("   구독: %s (%s/%s) %.0f원 | %s ~ %s\n",
                serviceName, category, plan, monthlyFee,
                startDate, endDate);
    }

    @Override
    public boolean matches(String kwd) {
        return serviceName.contains(kwd) || category.contains(kwd);
    }
}