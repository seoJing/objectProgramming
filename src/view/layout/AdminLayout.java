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

public class AdminLayout extends BaseLayout {

    @Override
    protected JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(UIConstants.HEADER_SIZE);
        header.setBackground(UIConstants.ADMIN_HEADER_COLOR);
        header.setBorder(UIConstants.HEADER_PADDING);

        JLabel titleLabel = new JLabel("관리자 페이지");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JPanel adminPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adminPanel.setOpaque(false);

        if (SessionManager.getInstance().isLoggedIn()) {
            String adminName = SessionManager.getInstance().getCurrentUser().getName();
            JLabel adminLabel = new JLabel("관리자: " + adminName);
            adminLabel.setForeground(UIConstants.WHITE);
            adminLabel.setFont(UIConstants.NORMAL_FONT);
            adminPanel.add(adminLabel);

            JButton logoutBtn = new JButton("로그아웃");
            logoutBtn.setFont(UIConstants.SMALL_FONT);
            logoutBtn.setFocusPainted(false);
            logoutBtn.addActionListener(e -> {
                SessionManager.getInstance().logout();
                Router.getInstance().navigateTo(Routes.ADMIN_LOGIN);
            });
            adminPanel.add(logoutBtn);
        }

        header.add(adminPanel, BorderLayout.EAST);

        return header;
    }

    @Override
    protected JPanel createNavigation() {
        JPanel nav = new JPanel(new GridLayout(1, 4));
        nav.setPreferredSize(UIConstants.NAV_SIZE);
        nav.setBackground(UIConstants.NAV_BACKGROUND_COLOR);

        nav.add(createNavButton("대시보드", Routes.ADMIN_DASHBOARD));
        nav.add(createNavButton("구독매칭", Routes.ADMIN_SUBSCRIPTION_MACHINE));
        nav.add(createNavButton("구독관리", Routes.ADMIN_SUBSCRIPTION_MANAGE));
        nav.add(createNavButton("통계분석", Routes.ADMIN_STATISTICS));

        return nav;
    }

    private JButton createNavButton(String text, String screen) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.NORMAL_FONT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        btn.addActionListener(e -> {
            Router.getInstance().navigateAdmin(screen);
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
