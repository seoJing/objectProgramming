import java.util.Scanner;

public interface Manageable {
    // 파일(또는 스캐너)로부터 데이터를 읽어 객체를 채웁니다.
    void read(Scanner scan);

    // 객체의 정보를 출력합니다.
    void print();

    // 주어진 키워드(kwd)와 객체의 데이터가 일치하는지 확인합니다. (검색용)
    boolean matches(String kwd);
}