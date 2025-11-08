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

    public MainFrame() {
        setTitle("구독 관리형 가계부");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(UIConstants.USER_SIDE_WINDOW_WIDTH, UIConstants.USER_SIDE_WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        UserSidePanel userPanel = new UserSidePanel();
        Router.getInstance().setUserSidePanel(userPanel);

        AdminSidePanel adminPanel = new AdminSidePanel();
        Router.getInstance().setAdminSidePanel(adminPanel);

        container.add(new LoginPanel(), Routes.LOGIN);
        container.add(userPanel, Routes.USER);
        container.add(adminPanel, Routes.ADMIN);

        add(container);

        cardLayout.show(container, Routes.LOGIN);
    }

    public void switchTo(String screen) {
        cardLayout.show(container, screen);

        // Resize window based on screen type
        if (screen.equals(Routes.ADMIN)) {
            setSize(UIConstants.ADMIN_SIDE_WINDOW_WIDTH, UIConstants.ADMIN_SIDE_WINDOW_HEIGHT);
        } else if (screen.equals(Routes.USER)) {
            setSize(UIConstants.USER_SIDE_WINDOW_WIDTH, UIConstants.USER_SIDE_WINDOW_HEIGHT);
        }
        setLocationRelativeTo(null);  // Center the window after resize
    }
}
