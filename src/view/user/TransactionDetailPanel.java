package view.user;

import model.Account;
import model.Transaction;
import model.TransactionType;
import util.Router;
import util.Routes;
import util.SessionManager;
import util.UIConstants;
import view.layout.UserLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class TransactionDetailPanel extends UserLayout {

    private static final DateTimeFormatter FULL =
            DateTimeFormatter.ofPattern("yyyy.MM.dd (E) HH:mm", java.util.Locale.KOREAN);

    private JLabel bankAndNumber;
    private JLabel when;
    private JLabel merchant;
    private JLabel category;
    private JLabel memo;
    private JLabel amount;
    private JLabel balanceAfter;

    public TransactionDetailPanel() {
        super();
        setContent(createContent());
        loadDataFromSession();

        // 보일 때마다 세션에서 다시 읽기 (캐시되어도 OK)
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentShown(java.awt.event.ComponentEvent e) {
                loadDataFromSession();
            }
        });
    }

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UIConstants.PANEL_BG);

        // 상단 바
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        top.setBorder(UIConstants.TOP_PANEL_BORDER);

        JButton back = new JButton("x");
        back.addActionListener(e -> {
            SessionManager sm = SessionManager.getInstance();
            if (sm.isFromAllTransactions()) {
                // 전체 거래 화면으로 돌아갈 때는 플래그를 한 번 쓰고 리셋
                sm.setFromAllTransactions(false);
                Router.getInstance().navigateUser(Routes.ALL_TRANSACTIONS);
            } else {
                // 기본: 계좌별 거래 화면으로
                Router.getInstance().navigateUser(Routes.TRANSACTION);
            }
        });
        top.add(back, BorderLayout.EAST);

        JLabel title = new JLabel("거래 상세");
        title.setFont(UIConstants.TITLE_FONT);
        title.setForeground(Color.DARK_GRAY);
        top.add(title, BorderLayout.CENTER);

        root.add(top, BorderLayout.NORTH);

        // 본문
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new GridBagLayout());
        body.setBorder(new EmptyBorder(16, 20, 24, 20));

        bankAndNumber = new JLabel(); bankAndNumber.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));
        when          = new JLabel(); when.setFont(UIConstants.NORMAL_FONT);
        merchant      = new JLabel(); merchant.setFont(UIConstants.TITLE_FONT);
        category      = new JLabel(); category.setFont(UIConstants.NORMAL_FONT);
        memo          = new JLabel(); memo.setFont(UIConstants.NORMAL_FONT);
        amount        = new JLabel(); amount.setFont(UIConstants.LARGE_FONT);
        balanceAfter  = new JLabel(); balanceAfter.setFont(UIConstants.NORMAL_FONT);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx=0;
        g.gridy=0;
        g.anchor=GridBagConstraints.WEST;
        g.insets=UIConstants.ZERO_INSETS;
        body.add(merchant, g);
        g.gridy++;
        g.insets=new Insets(16,0,6,0); body.add(amount, g);
        g.gridy++;
        body.add(category, g);
        g.gridy++;
        body.add(bankAndNumber, g);
        g.gridy++;
        body.add(when, g);
        g.gridy++;
        body.add(memo, g);
        g.gridy++;
        g.insets= UIConstants.ZERO_INSETS;
        body.add(balanceAfter, g);

        root.add(body, BorderLayout.CENTER);
        return root;
    }

    private void loadDataFromSession() {
        Account acc = SessionManager.getInstance().getSelectedAccount();
        Transaction tx = SessionManager.getInstance().getSelectedTransaction();
        if (acc == null || tx == null) {
            bankAndNumber.setText("선택된 거래가 없습니다.");
            return;
        }

        merchant.setText(safe(tx.getLocation()));
        boolean deposit = (tx.getType() == TransactionType.INCOME);
        String sign = deposit ? "+" : "-";
        amount.setText("금액: " + sign + UIConstants.money(tx.getAmount()) + "원");
        amount.setForeground(deposit ? UIConstants.POS_GREEN : UIConstants.TEXT_DEFAULT);
        category.setText("카테고리: " + safe(tx.getCategory()));
        when.setText("일시: " + tx.getDateTime().format(FULL));
        bankAndNumber.setText(acc.getBank() + " / " + acc.getAccountNumber());
        memo.setText("메모: " + safe(tx.getMemo()));

        balanceAfter.setText("거래 후 잔액: " + UIConstants.won(tx.getBalanceAfter()));

    }

    private static String safe(String s){ return (s==null) ? "" : s; }
}
