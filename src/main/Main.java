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
            MainFrame frame = new MainFrame();
            Router.getInstance().setMainFrame(frame);
            frame.setVisible(true);

            // ===== 로그인 방식 선택 =====
            // true: u001 계정으로 바로 로그인 (시연용)
            // false: 로그인 화면부터 시작 (정상 흐름)
            final boolean DEMO_MODE = true;

            if (DEMO_MODE) {
                seedDevLogin();
            }
        });
    }

    private static void seedDevLogin() {
        User user = UserList.getInstance().findById("u001");

        if (user != null) {
            SessionManager.getInstance().login(user);

            if (user.isAdmin()) {
                Router.getInstance().navigateTo(Routes.ADMIN);
            } else {
                Router.getInstance().navigateTo(Routes.USER);
            }
        }
    }
}


