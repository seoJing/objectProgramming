package view.layout;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Router;
import util.Routes;
import util.SessionManager;
import util.UIConstants;

public class UserLayout extends BaseLayout {

    @Override
    protected JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(UIConstants.HEADER_SIZE);
        header.setBackground(UIConstants.USER_HEADER_COLOR);
        header.setBorder(UIConstants.HEADER_PADDING);

        JLabel titleLabel = new JLabel("구독 관리형 가계부");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);

        if (SessionManager.getInstance().isLoggedIn()) {
            String userName = SessionManager.getInstance().getCurrentUser().getName();
            JLabel userLabel = new JLabel(userName + "님");
            userLabel.setForeground(UIConstants.WHITE);
            userLabel.setFont(UIConstants.NORMAL_FONT);
            userPanel.add(userLabel);

            JButton logoutBtn = new JButton("로그아웃");
            logoutBtn.setFont(UIConstants.SMALL_FONT);
            logoutBtn.setFocusPainted(false);
            logoutBtn.addActionListener(e -> {
                SessionManager.getInstance().logout();
                Router.getInstance().navigateTo(Routes.LOGIN);
            });
            userPanel.add(logoutBtn);
        }

        header.add(userPanel, BorderLayout.EAST);

        return header;
    }

    @Override
    protected JPanel createNavigation() {
        JPanel nav = new JPanel(new GridLayout(1, 5));
        nav.setPreferredSize(UIConstants.NAV_SIZE);
        nav.setBackground(UIConstants.NAV_BACKGROUND_COLOR);

        nav.add(createNavButton("메인", Routes.MAIN));
        nav.add(createNavButton("계좌", Routes.ACCOUNT));
        nav.add(createNavButton("구독", Routes.SUBSCRIPTION));
        nav.add(createNavButton("마이페이지", Routes.MY_PAGE));

        return nav;
    }

    private JButton createNavButton(String text, String screen) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.NORMAL_FONT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        btn.addActionListener(e -> {
            Router.getInstance().navigateUser(screen);
        });

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });

        return btn;
    }
}
