package view.user.shared.component;

import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
        // 스크롤 가능한 거래 내역 리스트
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(java.awt.Color.WHITE);
        listPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));

        for (Transaction transaction : transactions) {
            // TransactionItemPanel 사용
            TransactionItemPanel itemPanel = new TransactionItemPanel(transaction);
            listPanel.add(itemPanel);
            listPanel.add(Box.createVerticalStrut(8));
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
