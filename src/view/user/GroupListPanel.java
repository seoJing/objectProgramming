package view.user;

import static util.UIConstants.BACKGROUND_BUTTON;
import static util.UIConstants.BACKGROUND_LIGHT;
import static util.UIConstants.BUTTON_HORIZONTAL_GAP;
import static util.UIConstants.NORMAL_FONT;
import static util.UIConstants.SMALL_FONT;
import static util.UIConstants.TEXT_PRIMARY_COLOR;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import util.Router;
import util.Routes;
import view.layout.UserLayout;

/**
 * 두 번째 페이지 : 그룹핑 리스트
 */
public class GroupListPanel extends UserLayout {

    public GroupListPanel() {
        super();
        setContent(createContent());
    }

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND_LIGHT);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

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

        /* 이벤트 */

        // 장바구니 버튼 → 첫 페이지로 이동
        basketBtn.addActionListener(e -> Router.getInstance().navigateUser(Routes.STORE));

        // 큰 카드 클릭 → 상세 페이지
        groupPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // TODO: 그룹핑 상세 페이지로 이동
            }
        });

        root.add(center, BorderLayout.CENTER);
        return root;
    }

    private static void styleFlatButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(NORMAL_FONT);
        btn.setBackground(BACKGROUND_BUTTON);
        btn.setForeground(TEXT_PRIMARY_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }
}
