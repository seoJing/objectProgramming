package view.user;

import static util.UIConstants.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import view.user.shared.component.PanelHeader;
import view.user.shared.layout.UserLayout;

/**
 * 세 번째 페이지 : 그룹핑 상세
 */
public class GroupDetailPanel extends UserLayout {

    public GroupDetailPanel() {
        super();
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_LIGHT);

     
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
        center.add(Box.createVerticalStrut(16));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(BACKGROUND_BUTTON);
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        int infoHeight = 400;
        infoPanel.setPreferredSize(new Dimension(0, infoHeight));
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, infoHeight));

        JLabel infoLabel = new JLabel("<html>안내사항<br/>디테일<br/>환불 규정</html>");
        infoLabel.setFont(NORMAL_FONT.deriveFont(13f));
        infoLabel.setForeground(TEXT_PRIMARY_COLOR);
        infoPanel.add(infoLabel);

        center.add(infoPanel);
        center.add(Box.createVerticalGlue());

        JButton applyBtn = new JButton("그룹핑 신청");
        applyBtn.setBackground(BACKGROUND_BUTTON);
        applyBtn.setForeground(TEXT_PRIMARY_COLOR);
        applyBtn.setFocusPainted(false);
        applyBtn.setFont(NORMAL_FONT);
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyBtn.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        center.add(applyBtn);
        center.add(Box.createVerticalStrut(24));

        
        applyBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "그룹핑 신청이 완료되었습니다. (예시)",
                        "알림",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );
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
