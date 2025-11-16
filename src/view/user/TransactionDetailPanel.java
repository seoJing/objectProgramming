package view.user;

import model.Account;
import model.Transaction;
import model.TransactionType;
import service.AccountService; // 이미 프로젝트에 있을 거라 가정
import util.SessionManager;
import util.UIConstants;
import view.layout.UserLayout;
import view.user.shared.component.PanelHeader;
import view.user.shared.component.TransactionHistoryList;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TransactionDetailPanel extends UserLayout {

    public TransactionDetailPanel() {
        super();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            setContent(createContent());
        }
    }

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        Account account = SessionManager.getInstance().getSelectedAccount();
        Transaction selected = SessionManager.getInstance().getSelectedTransaction();

        // 헤더
        root.add(new PanelHeader("거래 상세"), BorderLayout.NORTH);

        if (account == null || selected == null) {
            JPanel empty = new JPanel();
            empty.setBackground(Color.WHITE);
            empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));

            JLabel msg = new JLabel("선택된 거래가 없습니다.");
            msg.setFont(UIConstants.NORMAL_FONT);
            msg.setAlignmentX(Component.CENTER_ALIGNMENT);

            empty.add(Box.createVerticalGlue());
            empty.add(msg);
            empty.add(Box.createVerticalGlue());

            root.add(empty, BorderLayout.CENTER);
            return root;
        }

        // ===== 상단(제목 + 큰 금액) + 카드 하나 =====
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(32, 48, 24, 48));

        // 1) 제목 + 큰 금액
        center.add(createTitleSection(selected), BorderLayout.NORTH);

        // 2) 아래 카드 리스트 (선택된 거래 1개만)
        List<Transaction> list = Collections.singletonList(selected);
        TransactionHistoryList historyList = new TransactionHistoryList(list);
        center.add(historyList, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);

        return root;
    }

    /** 상단의 가맹점 이름 + 큰 금액 영역 */
    private JComponent createTitleSection(Transaction tx) {
        JPanel box = new JPanel();
        box.setBackground(Color.WHITE);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));

        // 가맹점 이름
        JLabel merchant = new JLabel(safe(tx.getLocation()));
        merchant.setFont(UIConstants.LARGE_FONT);
        merchant.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 금액 (부호 + 색상)
        boolean deposit = (tx.getType() == TransactionType.INCOME);
        String sign = deposit ? "+" : "-";
        int absAmount = Math.abs(tx.getAmount());

        JLabel amount = new JLabel(sign + UIConstants.money(absAmount) + "원");
        amount.setFont(UIConstants.TITLE_FONT);
        amount.setForeground(deposit ? UIConstants.POS_GREEN : UIConstants.TEXT_PRIMARY_COLOR);
        amount.setAlignmentX(Component.LEFT_ALIGNMENT);

        box.add(merchant);
        box.add(Box.createVerticalStrut(8));
        box.add(amount);
        box.add(Box.createVerticalStrut(24));

        return box;
    }

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}
