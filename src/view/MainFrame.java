package view;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import util.Router;
import util.Routes;
import util.UIConstants;
import view.login.LoginPanel;
import view.user.UserSidePanel;
import view.admin.AdminSidePanel;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel container;
    private UserSidePanel userPanel;
    private AdminSidePanel adminPanel;

    public MainFrame() {
        setTitle("구독 관리형 가계부");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(UIConstants.USER_SIDE_WINDOW_WIDTH, UIConstants.USER_SIDE_WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        // Router에 메인 프레임 등록
        Router.getInstance().setMainFrame(this);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // 초기 화면 구성 (로그인 화면만 실제 화면)
        container.add(new LoginPanel(), Routes.LOGIN);
        container.add(new JPanel(), Routes.USER);   // 로그인 후 UserSidePanel로 교체될 자리
        container.add(new JPanel(), Routes.ADMIN);  // 로그인 후 AdminSidePanel로 교체될 자리

        add(container);

        // 초기 화면: 로그인 화면
        cardLayout.show(container, Routes.LOGIN);
    }

    /** 
     * 로그인 후 동적으로 화면을 교체하기 위한 메서드 
     */
    public void addScreen(String route, JPanel panel) {
        container.add(panel, route);
    }

    public void switchTo(String screen) {
        cardLayout.show(container, screen);

        // 화면 크기 조정
        if (screen.equals(Routes.ADMIN)) {
            setSize(UIConstants.ADMIN_SIDE_WINDOW_WIDTH, UIConstants.ADMIN_SIDE_WINDOW_HEIGHT);
        } else if (screen.equals(Routes.USER)) {
            setSize(UIConstants.USER_SIDE_WINDOW_WIDTH, UIConstants.USER_SIDE_WINDOW_HEIGHT);
        }

        setLocationRelativeTo(null);  // 창 가운데 정렬
    }

    public void showUserView() {
        if (userPanel == null) {
            userPanel = new UserSidePanel();
            container.add(userPanel, Routes.USER);
            Router.getInstance().setUserSidePanel(userPanel);
        }
        switchTo(Routes.USER);
    }

    public void showAdminView() {
        if (adminPanel == null) {
            adminPanel = new AdminSidePanel();
            container.add(adminPanel, Routes.ADMIN);
            Router.getInstance().setAdminSidePanel(adminPanel);
        }
        switchTo(Routes.ADMIN);
    }
}
