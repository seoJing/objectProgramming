package main;

import javax.swing.SwingUtilities;

import model.User;
import model.UserList;
import util.DataLoader;
import util.Router;
import util.Routes;
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

            MainFrame frame = new MainFrame();
            Router.getInstance().setMainFrame(frame);
            frame.setVisible(true);

            // ===== 로그인 방식 선택 =====
            // true: u001 계정으로 바로 로그인 (시연용)
            // false: 로그인 화면부터 시작 (정상 흐름)
            final boolean DEMO_MODE = true;

            if (DEMO_MODE) {
                seedDevLogin();    // u001로 바로 로그인
            }
        });
    }

    private static void seedDevLogin() {
        // DataLoader에서 로드된 실제 u001 사용자 조회
        User user = UserList.getInstance().findById("u001");

        if (user != null) {
            // 파일에서 로드된 실제 사용자로 로그인
            SessionManager.getInstance().login(user);

            // 로그인 후 화면 전환
            if (user.isAdmin()) {
                Router.getInstance().navigateTo(Routes.ADMIN);
            } else {
                Router.getInstance().navigateTo(Routes.USER);
            }
        }
    }
}


