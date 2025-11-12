package view.user.shared.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Transaction;
import util.UIConstants;

/**
 * 거래 항목을 표시하는 패널 컴포넌트
 */
public class TransactionItemPanel extends JPanel {

    public TransactionItemPanel(Transaction transaction) {
        super(new BorderLayout());

        setBackground(new Color(240, 245, 250));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        setOpaque(false);

        // 왼쪽: 날짜 및 설명
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel dateLabel = new JLabel(transaction.getDate());
        dateLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 11));
        dateLabel.setForeground(new Color(120, 120, 120));
        leftPanel.add(dateLabel);

        JLabel descriptionLabel = new JLabel(transaction.getBank());
        descriptionLabel.setFont(UIConstants.SMALL_FONT);
        descriptionLabel.setForeground(new Color(51, 51, 51));
        leftPanel.add(descriptionLabel);

        add(leftPanel, BorderLayout.WEST);

        // 오른쪽: 금액
        JLabel amountLabel = new JLabel(String.format("%,d원", transaction.getAmount()));
        amountLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 12));
        amountLabel.setForeground(new Color(11, 218, 81));
        add(amountLabel, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        super.paintComponent(g);
    }
}
