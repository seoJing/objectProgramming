package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import model.UserList;
import util.Router;
import util.Routes;
import util.UIConstants;
import view.admin.*;
import view.login.LoginPanel;
import view.user.UserSidePanel;

public class MainFrame extends JFrame implements ActionListener {

    private CardLayout cardLayout;
    private JPanel container;

    private JPanel adminContent;   // ⭐ 관리자 내부 패널 표시할 영역

    public MainFrame() {
        setTitle("구독 관리형 가계부");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(UIConstants.USER_SIDE_WINDOW_WIDTH, UIConstants.USER_SIDE_WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        // Router 등록
        Router.getInstance().setMainFrame(this);

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // ======================
        // USER 화면
        // ======================
        UserSidePanel userPanel = new UserSidePanel();
        Router.getInstance().setUserSidePanel(userPanel);

        // ======================
        // ADMIN 화면 (사이드 + 콘텐츠)
        // ======================
        JPanel adminPanel = new JPanel(new BorderLayout());

        AdminSidePanel side = new AdminSidePanel(this); // 버튼 이벤트 MainFrame으로 전달
        adminContent = new JPanel(new CardLayout());

        // 관리자 내부 패널 등록
        adminContent.add(new AdminMainPanel(), Routes.ADMIN_MAIN);
        adminContent.add(new AdminUserView(UserList.getInstance()), Routes.ADMIN_USERS);
        adminContent.add(new AdminSubscriptionManagePanel(), Routes.ADMIN_SUB_MANAGE);
        adminContent.add(new AdminStatisticsPanel(), Routes.ADMIN_STATISTICS);

        adminPanel.add(side, BorderLayout.WEST);
        adminPanel.add(adminContent, BorderLayout.CENTER);

        // container 등록
        container.add(new LoginPanel(), Routes.LOGIN);
        container.add(userPanel, Routes.USER);
        container.add(adminPanel, Routes.ADMIN);

        add(container);

        cardLayout.show(container, Routes.LOGIN);
    }

    // 전체 화면 전환
    public void switchTo(String screen) {
        cardLayout.show(container, screen);

        if (screen.equals(Routes.ADMIN)) {
            setSize(UIConstants.ADMIN_SIDE_WINDOW_WIDTH, UIConstants.ADMIN_SIDE_WINDOW_HEIGHT);
        } else if (screen.equals(Routes.USER)) {
            setSize(UIConstants.USER_SIDE_WINDOW_WIDTH, UIConstants.USER_SIDE_WINDOW_HEIGHT);
        }
        setLocationRelativeTo(null);
    }

    // 관리자 내부 패널 전환
    public void switchAdminContent(String target) {
        CardLayout cl = (CardLayout) adminContent.getLayout();
        cl.show(adminContent, target);
    }

    // 이벤트 처리
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {

            case "MAIN":
                switchAdminContent(Routes.ADMIN_MAIN);
                break;

            case "USER_VIEW":
                switchAdminContent(Routes.ADMIN_USERS);
                break;

            case "SUB_MANAGE":
                switchAdminContent(Routes.ADMIN_SUB_MANAGE);
                break;

            case "STATISTICS":
                switchAdminContent(Routes.ADMIN_STATISTICS);
                break;

            case "LOGOUT":
                switchTo(Routes.LOGIN);
                break;
        }
    }
}
