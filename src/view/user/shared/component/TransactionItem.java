package view.user.shared.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Account;
import model.Transaction;
import model.TransactionType;
import util.SessionManager;
import util.UIConstants;

/**
 * 거래 항목을 표시하는 패널 컴포넌트
 * (거래 상세 / 구독 상세 둘 다에서 사용)
 */
public class TransactionItem extends JPanel {

    public TransactionItem(Transaction transaction) {
        super(new BorderLayout());

        // 카드 스타일
        setBackground(UIConstants.CARD_BG);
        setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        setOpaque(false);

        // ===== 왼쪽: 날짜 / 계좌 / 카테고리 · 메모 =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        // 1) 날짜 / 시간 (Transaction.getDate() 는 이미 포맷된 문자열이라고 가정)
        JLabel dateLabel = new JLabel(transaction.getDate());
        dateLabel.setFont(UIConstants.SMALL_FONT);
        dateLabel.setForeground(UIConstants.TEXT_MUTED);
        leftPanel.add(dateLabel);

        // 2) 은행 / 계좌번호 (SessionManager 의 선택 계좌 사용)
        Account acc = SessionManager.getInstance().getSelectedAccount();
        String bankLine = "";
        if (acc != null) {
            bankLine = acc.getBank() + " / " + acc.getAccountNumber();
        }
        JLabel accountLabel = new JLabel(bankLine);
        accountLabel.setFont(UIConstants.NORMAL_FONT);
        accountLabel.setForeground(UIConstants.TEXT_DEFAULT);
        leftPanel.add(accountLabel);

        // 3) 카테고리 · 메모
        String extra = "카테고리: " + safe(transaction.getCategory());
        String memo = safe(transaction.getMemo());
        if (!memo.isEmpty()) {
            extra += "  ·  메모: " + memo;
        }
        JLabel extraLabel = new JLabel(extra);
        extraLabel.setFont(UIConstants.SMALL_FONT);
        extraLabel.setForeground(UIConstants.TEXT_MUTED);
        leftPanel.add(extraLabel);

        add(leftPanel, BorderLayout.WEST);

        // ===== 오른쪽: 금액 (+초록 / -검정) =====
        boolean deposit = (transaction.getType() == TransactionType.INCOME);
        String sign = deposit ? "+" : "-";
        int absAmount = Math.abs(transaction.getAmount());

        JLabel amountLabel = new JLabel(sign + UIConstants.money(absAmount) + "원");
        amountLabel.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));
        amountLabel.setForeground(deposit ? UIConstants.POS_GREEN : UIConstants.TEXT_DEFAULT);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(amountLabel, BorderLayout.EAST);

        add(rightPanel, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
        super.paintComponent(g);
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}
