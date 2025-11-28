package view;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import util.Router;
import util.Routes;
import util.UIConstants;
import view.admin.AdminSidePanel;
import view.login.LoginPanel;
import view.user.UserSidePanel;

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

        Router.getInstance().setMainFrame(this);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        container.add(new LoginPanel(), Routes.LOGIN);
        container.add(new JPanel(), Routes.USER);
        container.add(new JPanel(), Routes.ADMIN);

        add(container);

        cardLayout.show(container, Routes.LOGIN);
    }


    public void switchTo(String screen) {
        cardLayout.show(container, screen);

        if (screen.equals(Routes.ADMIN)) {
            setSize(UIConstants.ADMIN_SIDE_WINDOW_WIDTH, UIConstants.ADMIN_SIDE_WINDOW_HEIGHT);
        } else if (screen.equals(Routes.USER)) {
            setSize(UIConstants.USER_SIDE_WINDOW_WIDTH, UIConstants.USER_SIDE_WINDOW_HEIGHT);
        }

        setLocationRelativeTo(null);
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
