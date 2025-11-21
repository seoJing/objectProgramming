package view.layout;

import java.awt.*;

import javax.swing.*;

import util.Router;
import util.Routes;
import util.SessionManager;
import util.UIConstants;

public class UserLayout extends BaseLayout {

    @Override
    protected JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 하단 그림자
                int shadowHeight = 8;
                for (int i = 0; i < shadowHeight; i++) {
                    float alpha = (float) (0.15 * (1 - (i / (float) shadowHeight)));
                    g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                    g2d.drawLine(0, getHeight() - shadowHeight + i,
                            getWidth(), getHeight() - shadowHeight + i);
                }
            }
        };
        header.setPreferredSize(UIConstants.HEADER_SIZE);
        header.setBackground(UIConstants.HEADER_BACKGROUND_COLOR);
        header.setBorder(UIConstants.HEADER_PADDING);

        /* ===== 왼쪽: subMate (왼쪽 정렬 + 세로 가운데) ===== */
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("subMate");
        titleLabel.setFont(UIConstants.TITLE_FONT);
        titleLabel.setForeground(UIConstants.USER_HEADER_COLOR);

        leftPanel.add(titleLabel);
        header.add(leftPanel, BorderLayout.WEST);

        /* ===== 오른쪽: 알람 / 설정 / 이름 / 로그아웃 ===== */
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        rightPanel.setOpaque(false);

        // 공통 스타일 버튼 (작은 글씨 + 여백 제거)
        java.util.function.Function<String, JButton> makeHeaderButton = text -> {
            JButton btn = new JButton(text);
            btn.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 12)); // 작게
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder());  // 테두리/여백 제거
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return btn;
        };

        JButton alertBtn = makeHeaderButton.apply("알람");
        alertBtn.addActionListener(e -> Router.getInstance().navigateUser(Routes.ALERT));
        rightPanel.add(alertBtn);

        JButton settingBtn = makeHeaderButton.apply("설정");
        settingBtn.addActionListener(e -> Router.getInstance().navigateUser(Routes.SETTING));
        rightPanel.add(settingBtn);

        if (SessionManager.getInstance().isLoggedIn()) {
            String userName = SessionManager.getInstance().getCurrentUser().getName();
            JLabel userLabel = new JLabel(userName + "님");
            userLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 12));
            userLabel.setForeground(UIConstants.TEXT_SECONDARY_COLOR);
            rightPanel.add(userLabel);
            JButton logoutBtn = makeHeaderButton.apply("로그아웃");
            logoutBtn.setForeground(UIConstants.TEXT_SECONDARY_COLOR);
            logoutBtn.addActionListener(e -> {
                SessionManager.getInstance().logout();
                Router.getInstance().navigateTo(Routes.LOGIN);
            });
            rightPanel.add(logoutBtn);
        }

        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    @Override
    protected JPanel createNavigation() {
        // 그림자를 위한 외부 패널
        JPanel shadowPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 상단에 퍼진 그림자 효과
                int shadowHeight = 8;
                for (int i = 0; i < shadowHeight; i++) {
                    float alpha = (float) (0.15 * (1 - (i / (float) shadowHeight)));
                    g2d.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
                    g2d.drawLine(0, i, getWidth(), i);
                }
            }
        };
        shadowPanel.setBackground(new Color(0, 0, 0, 0)); // 투명 배경
        shadowPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));

        // 내부 네비게이션 패널
        JPanel nav = new JPanel(new GridLayout(1, 5));
        nav.setPreferredSize(UIConstants.NAV_SIZE);
        nav.setBackground(UIConstants.NAV_BACKGROUND_COLOR);

        nav.add(createNavButton("메인", Routes.MAIN));
        nav.add(createNavButton("계좌", Routes.ACCOUNT));
        nav.add(createNavButton("구독", Routes.SUBSCRIPTION));
        nav.add(createNavButton("스토어", Routes.STORE));

        shadowPanel.add(nav, BorderLayout.CENTER);
        return shadowPanel;
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
