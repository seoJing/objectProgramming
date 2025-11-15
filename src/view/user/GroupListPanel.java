package view.user;

import static util.UIConstants.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import view.user.shared.component.PanelHeader;
import view.user.shared.layout.UserLayout;
import view.user.NavigateUser;

/**
 * 두 번째 페이지 : 그룹핑 리스트
 */
public class GroupListPanel extends UserLayout {

    public GroupListPanel() {
        super();
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_LIGHT);

        PanelHeader header = new PanelHeader("SUBMATE");
        header.setPreferredSize(HEADER_SIZE);
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(center, BorderLayout.CENTER);

        JLabel label = new JLabel("구독 장바구니", SwingConstants.CENTER);
        label.setFont(SMALL_FONT.deriveFont(13f));
        label.setForeground(TEXT_PRIMARY_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(label);
        center.add(Box.createVerticalStrut(8));

        // 위 버튼 줄: 첫 페이지와 동일
        JPanel modePanel = new JPanel(new GridLayout(1, 2, BUTTON_HORIZONTAL_GAP, 0));
        modePanel.setOpaque(false);
        JButton basketBtn = new JButton("장바구니");
        JButton groupBtn  = new JButton("그룹핑");
        styleFlatButton(basketBtn);
        styleFlatButton(groupBtn);
        modePanel.add(basketBtn);
        modePanel.add(groupBtn);
        center.add(modePanel);
        center.add(Box.createVerticalStrut(16));

        // 아래 큰 카드: 첫 페이지의 리스트+총액을 합친 느낌
        JPanel groupPanel = new JPanel(new BorderLayout());
        groupPanel.setBackground(BACKGROUND_BUTTON);

        int mergedHeight = 260 + 16 + 120; // 두 카드 합친 높이 근사
        groupPanel.setPreferredSize(new Dimension(0, mergedHeight));
        groupPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, mergedHeight));

        JLabel groupLabel = new JLabel("n/4 예시1 묶음 가격");
        groupLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        groupLabel.setFont(NORMAL_FONT.deriveFont(13f));
        groupLabel.setForeground(TEXT_PRIMARY_COLOR);
        groupPanel.add(groupLabel, BorderLayout.NORTH);

        center.add(groupPanel);
        center.add(Box.createVerticalGlue());

        // 하단 네비
        JPanel bottomNav = new JPanel(new GridLayout(1, 4));
        bottomNav.setPreferredSize(NAV_SIZE);
        bottomNav.setBackground(POS_GREEN);
        bottomNav.add(createNavButton("홈", false));
        bottomNav.add(createNavButton("계좌", false));
        bottomNav.add(createNavButton("내 구독", true));
        bottomNav.add(createNavButton("구독 스토어", false));
        add(bottomNav, BorderLayout.SOUTH);

        /* 이벤트 */

        // 장바구니 버튼 → 첫 페이지로 이동
        basketBtn.addActionListener(e -> NavigateUser.goTo(new SubscriptionAddPanel()));

        // 큰 카드 클릭 → 상세 페이지
        groupPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                NavigateUser.goTo(new GroupDetailPanel());
            }
        });
    }

    /* ===== 스타일 함수 (복사본) ===== */

    private static JButton createNavButton(String text, boolean selected) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(NORMAL_FONT);
        if (selected) {
            btn.setBackground(WHITE);
            btn.setForeground(POS_GREEN);
        } else {
            btn.setBackground(POS_GREEN);
            btn.setForeground(WHITE);
        }
        return btn;
    }

    private static void styleFlatButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(NORMAL_FONT);
        btn.setBackground(BACKGROUND_BUTTON);
        btn.setForeground(TEXT_PRIMARY_COLOR);
        Border padding = BorderFactory.createEmptyBorder(8, 8, 8, 8);
        btn.setBorder(padding);
    }
}
