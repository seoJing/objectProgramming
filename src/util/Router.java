package util;

import view.MainFrame;
import view.admin.AdminSidePanel;
import view.user.UserSidePanel;

public class Router {

    private static Router instance;

    private MainFrame mainFrame;
    private UserSidePanel userSidePanel;
    private AdminSidePanel adminSidePanel;

    private Router() {}

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    // ================== 메인 프레임 등록 ==================
    public void setMainFrame(MainFrame frame) {
        this.mainFrame = frame;
    }

    public MainFrame getMainFrame() {
        return this.mainFrame;
    }

    // ================== 화면 패널 저장 ==================
    public void setUserSidePanel(UserSidePanel panel) {
        this.userSidePanel = panel;
    }

    public void setAdminSidePanel(AdminSidePanel panel) {
        this.adminSidePanel = panel;
    }

    // ================== 전체 화면 전환 ==================
    public void navigateTo(String screen) {
        if (mainFrame == null) return;

        if (screen.equals(Routes.ADMIN)) {
            mainFrame.showAdminView();
        } else if (screen.equals(Routes.USER)) {
            mainFrame.showUserView();
        } else {
            mainFrame.switchTo(screen);
        }
    }

    // ================== 유저 화면 내부 전환 ==================
    public void navigateUser(String screen) {
        if (userSidePanel != null) {
            userSidePanel.switchTo(screen);
        } else {
            System.err.println("[Router] UserSidePanel is null! (로그인 전 호출?)");
        }
    }

    // ================== 관리자 화면 내부 전환 ==================
    public void navigateAdmin(String screen) {
        if (adminSidePanel != null) {
            adminSidePanel.switchTo(screen);
        } else {
            System.err.println("[Router] AdminSidePanel is null! (로그인 전 호출?)");
        }
    }
}
