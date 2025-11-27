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

    public void setMainFrame(MainFrame frame) {
        this.mainFrame = frame;
    }

    public MainFrame getMainFrame() {
        return this.mainFrame;
    }

    public void setUserSidePanel(UserSidePanel panel) {
        this.userSidePanel = panel;
    }

    public void setAdminSidePanel(AdminSidePanel panel) {
        this.adminSidePanel = panel;
    }

    public void navigateTo(String screen) {
        if (mainFrame == null) return;

        System.out.println("→ " + screen + " 으로 이동");
        if (screen.equals(Routes.ADMIN)) {
            mainFrame.showAdminView();
        } else if (screen.equals(Routes.USER)) {
            mainFrame.showUserView();
        } else {
            mainFrame.switchTo(screen);
        }
    }

    public void navigateUser(String screen) {
        System.out.println("→ " + screen + " 으로 이동");
        if (userSidePanel != null) {
            userSidePanel.switchTo(screen);
        } else {
            System.err.println("[Router] UserSidePanel is null! (로그인 전 호출?)");
        }
    }

    public void navigateAdmin(String screen) {
        System.out.println("→ " + screen + " (관리자)로 이동");
        if (adminSidePanel != null) {
            adminSidePanel.switchTo(screen);
        } else {
            System.err.println("[Router] AdminSidePanel is null! (로그인 전 호출?)");
        }
    }
}
