package view.user.shared.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.Transaction;

/**
 * 거래 내역 리스트를 표시하는 스크롤 가능한 패널 컴포넌트
 * 데이터는 외부에서 주입받고, UI만 렌더링함
 */
public class TransactionHistoryList extends JScrollPane {

    public TransactionHistoryList(List<Transaction> transactions) {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(java.awt.Color.WHITE);
        listPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));

        if (transactions.isEmpty()) {
            listPanel.add(Box.createVerticalGlue());

            JLabel placeholderLabel = new JLabel("거래내역이 없습니다");
            placeholderLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            placeholderLabel.setForeground(new Color(150, 150, 150));
            placeholderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(placeholderLabel);

            listPanel.add(Box.createVerticalGlue());
        } else {
            for (Transaction transaction : transactions) {
                TransactionItem itemPanel = new TransactionItem(transaction);
                listPanel.add(itemPanel);
                listPanel.add(Box.createVerticalStrut(8));
            }
        }

        setViewportView(listPanel);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        setBorder(null);
        getVerticalScrollBar().setUnitIncrement(10);
        setPreferredSize(new Dimension(getPreferredSize().width, 280));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
    }
}
