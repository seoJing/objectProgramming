package view.user.shared.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import util.UIConstants;

/**
 * Rounded rectangle 스타일의 커스텀 버튼 컴포넌트
 * 데이터는 외부에서 주입받고, UI만 렌더링함
 */
public class RoundedButton extends JButton {

    public RoundedButton(String text, Color bgColor) {
        super(text);
        initButton(bgColor);
    }

    public RoundedButton(String text, Color bgColor, ActionListener onClickListener) {
        super(text);
        initButton(bgColor);
        if (onClickListener != null) {
            addActionListener(onClickListener);
        }
    }

    private void initButton(Color bgColor) {
        setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBackground(bgColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        setPreferredSize(new Dimension(150, 44));
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
        super.paintComponent(g);
    }
}
