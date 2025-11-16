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

    // ================== 초기 설정 ==================
    public void setMainFrame(MainFrame frame) {
        this.mainFrame = frame;
    }

    public void setUserSidePanel(UserSidePanel panel) {
        this.userSidePanel = panel;
    }

    public void setAdminSidePanel(AdminSidePanel panel) {
        this.adminSidePanel = panel;
    }

    // ================== 전체 화면 전환 (프레임 교체) ==================
    public void navigateTo(String screen) {
        if (mainFrame != null) {
            mainFrame.switchTo(screen);
        }
    }

    // ================== 유저 네비게이션 ==================
    public void navigateUser(String screen) {
        if (userSidePanel != null) {
            userSidePanel.switchTo(screen);
        }
    }

    // ================== 관리자 네비게이션 ==================
    public void navigateAdmin(String screen) {
        if (adminSidePanel != null) {
            adminSidePanel.switchTo(screen);
        }
    }

    
}
