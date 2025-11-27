package view.user.shared.component;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDateTime;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Account;
import model.Transaction;
import model.TransactionType;
import util.UIConstants;

// TransactionItemRow - 거래 항목 표시 컴포넌트 / (Account, Transaction, LocalDateTime → JPanel)
public class TransactionItemRow {

    private final Account account;
    private final Transaction transaction;
    private final LocalDateTime dateTime;

    public TransactionItemRow(Account account, Transaction transaction, LocalDateTime dateTime) {
        this.account = account;
        this.transaction = transaction;
        this.dateTime = dateTime;
    }

    public JPanel createRow(Runnable onRowClick) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(true);
        row.setBackground(UIConstants.TX_ROW_BG);
        row.setBorder(UIConstants.TX_ROW_BORDER);
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        row.putClientProperty("account", account);
        row.putClientProperty("tx", transaction);

        // 마우스 이벤트
        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (onRowClick != null) {
                    onRowClick.run();
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                row.setBackground(row.getBackground().darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                row.setBackground(UIConstants.TX_ROW_BG);
            }
        });

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.insets = UIConstants.ZERO_INSETS;

        // 왼쪽 (아이콘 영역)
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));

        JLabel icon = new JLabel(loadIcon("/resources/images/bag.png", 22, 22));
        left.add(Box.createHorizontalStrut(6));
        left.add(icon);
        left.setBorder(UIConstants.ICON_LEFT_PADDING);

        gc.gridx = 0;
        gc.weightx = 0;
        gc.anchor = GridBagConstraints.WEST;
        row.add(left, gc);

        // 중앙 (정보)
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(safe(transaction.getLocation()));
        title.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));

        JLabel time = new JLabel(dateTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm")));
        time.setFont(UIConstants.SMALL_FONT);
        time.setForeground(UIConstants.TEXT_MUTED);

        String sub = safe(transaction.getCategory()) + "  |  " + account.getBank() + " / " + account.getAccountNumber();
        JLabel subLabel = new JLabel(sub);
        subLabel.setFont(UIConstants.SMALL_FONT);
        subLabel.setForeground(UIConstants.TEXT_MUTED);

        center.add(title);
        center.add(Box.createVerticalStrut(2));
        center.add(time);
        center.add(Box.createVerticalStrut(4));
        center.add(subLabel);

        gc.gridx = 1;
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;
        row.add(center, gc);

        // 오른쪽 (금액)
        boolean deposit = (transaction.getType() == TransactionType.INCOME);
        String sign = deposit ? "+" : "-";
        JLabel amount = new JLabel(sign + UIConstants.money(transaction.getAmount()) + "원");
        amount.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));
        amount.setForeground(deposit ? UIConstants.POS_GREEN : UIConstants.TEXT_DEFAULT);

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(amount, BorderLayout.EAST);

        gc.gridx = 2;
        gc.weightx = 0;
        gc.anchor = GridBagConstraints.EAST;
        gc.insets = UIConstants.RIGHT_GAP_M;
        row.add(right, gc);

        return row;
    }

    public Account getAccount() {
        return account;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    private static String safe(String s) {
        return s == null || s.isBlank() ? "-" : s;
    }

    private static ImageIcon loadIcon(String path, int w, int h) {
        return ImageLoader.loadFromResource(path, w, h);
    }
}
