package orientedjava;

import java.util.*;
import java.io.*;

public class AdminManager {
    ArrayList<Manageable> list = new ArrayList<>();

    // 간단히 파일에서 데이터 읽어오기
    public void readAll(String filename, int type) {
        try (Scanner filein = new Scanner(new File(filename))) {
            while (filein.hasNext()) {
                Manageable m = create(type);
                m.read(filein);
                list.add(m);
            }
        } catch (FileNotFoundException e) {
            System.out.println("⚠️ 파일을 찾을 수 없습니다: " + filename);
        }
    }

    // type별 객체 생성
    private Manageable create(int type) {
        switch (type) {
            case 1: return new User();
            case 2: return new AccountBook();
            case 3: return new AccountItem();
            case 4: return new Subscription();
            default: return null;
        }
    }

    public void add(Manageable m) {
        list.add(m);
    }

    public void printAll() {
        for (Manageable m : list)
            m.print();
    }

    public void search(String kwd) {
        for (Manageable m : list)
            if (m.matches(kwd))
                m.print();
    }
}