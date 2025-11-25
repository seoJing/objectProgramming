package main;

import javax.swing.SwingUtilities;

import model.Account;
import model.User;
import model.UserList;
import util.Router;
import util.SessionManager;
import view.MainFrame;
import util.DataLoader;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DataLoader.loadAll();

            /*테스트
            System.out.println(">> 현재 로딩된 회원 수: " + UserList.getInstance().size() + "명");
            int totalTx = UserList.getInstance().getAll().stream().mapToInt(u -> u.getAccountList().stream().mapToInt(a -> a.getTransactionList().size()).sum()).sum();
            System.out.println(">>> 총 거래내역 수: " + totalTx + "건");
            UserList.getInstance().getAll().stream()
                    .flatMap(u -> u.getAccountList().stream())
                    .flatMap(a -> a.getTransactionList().stream())
                    .forEach(System.out::println);*/

            seedDevLogin();
            MainFrame frame = new MainFrame();
            Router.getInstance().setMainFrame(frame);
            frame.setVisible(true);
        });
    }

    /** 개발 편의를 위한 임시 로그인 세팅 */
    private static void seedDevLogin() {
        User u = new User("u001", "pw", "서징규", "M", 21, "student", "Seoul", "010-0000-0000", false);
        Account a1 = new Account("111-222-333", "신한은행", 200_000);
        u.addAccount(a1);                    // 유저에 계좌 연결
        SessionManager.getInstance().login(u);   // ★ 현재 사용자 등록

        //임시로 유저 보는 코드
        model.UserList.getInstance().add(u);


    }
}
