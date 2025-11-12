package view.user.shared.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.UIConstants;

/**
 * 구독, 구독 상세보기, 알람 패널 등에서 사용되는 공통 헤더 컴포넌트
 */
public class PanelHeader extends JPanel {

    public PanelHeader(String title) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 60));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 33, 33));

        add(titleLabel, BorderLayout.WEST);
    }
}
