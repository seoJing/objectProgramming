package main;

import javax.swing.SwingUtilities;

import model.User;
import model.UserList;
import util.DataLoader;
import util.Router;
import util.SessionManager;
import view.MainFrame;

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

            // seedDevLogin();    // 개발 편의를 위한 임시 로그인 세팅
        });
    }

    /** 개발 편의를 위한 임시 로그인 세팅 - 파일에서 로드된 실제 u001 사용자로 자동 로그인 */
    private static void seedDevLogin() {
        // DataLoader에서 로드된 실제 u001 사용자 조회
        User user = UserList.getInstance().findById("u001");

        if (user != null) {
            // 파일에서 로드된 실제 사용자로 로그인
            SessionManager.getInstance().login(user);
        }
    }
    */
}


