package orientedjava;

import java.util.Scanner;

/**
 * 모든 데이터 클래스의 기본 인터페이스
 */
public interface Manageable {
    void read(Scanner scan);
    void print();
    boolean matches(String kwd);
}