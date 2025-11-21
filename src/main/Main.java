package main;

import javax.swing.SwingUtilities;

import model.Account;
import model.User;
import util.Router;
import util.SessionManager;
import view.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
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
        SessionManager.getInstance().setCurrentUser(u);   // ★ 현재 사용자 등록
    }
}
