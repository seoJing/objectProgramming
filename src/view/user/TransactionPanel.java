package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Account;
import model.Transaction;
import model.TransactionType;
import model.User;
import util.Router;
import util.Routes;
import util.SessionManager;
import util.UIConstants;
import view.layout.UserLayout;
import view.user.shared.component.TransactionItemRow;

public class TransactionPanel extends UserLayout {

    private JComboBox<Account> accountCombo;
    private JLabel balanceLabel;

    private JPanel listPanel;
    private JScrollPane scroll;

    private Account initialSelection;

    private static final DateTimeFormatter HEADER_DOW =
            DateTimeFormatter.ofPattern("d일 E요일", Locale.KOREAN);


    public TransactionPanel() {
        this(null);
    }

    public TransactionPanel(Account preselected) {
        super();
        this.initialSelection = preselected;
        setContent(createContent());
        loadAccountsAndData();

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentShown(java.awt.event.ComponentEvent e) {
                syncSelectionFromSession();
            }
        });
    }

    // 세션에 저장된 계좌로 콤보/목록 동기화
    private void syncSelectionFromSession() {
        Account s = SessionManager.getInstance().getSelectedAccount();
        if (s == null) return;
        selectByAccountNumber(s.getAccountNumber());
        rebuildList((Account) accountCombo.getSelectedItem());
    }

    // 계좌번호로만 콤보에서 선택
    private void selectByAccountNumber(String accNo) {
        if (accNo == null) return;
        for (int i = 0; i < accountCombo.getItemCount(); i++) {
            Account a = accountCombo.getItemAt(i);
            if (accNo.equals(a.getAccountNumber())) {
                accountCombo.setSelectedIndex(i);
                SessionManager.getInstance().setSelectedAccount(a);
                return;
            }
        }
    }

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(true);
        root.setBackground(Color.WHITE);

        // 상단: 계좌 선택 + 잔액
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        top.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        top.setBorder(UIConstants.TOP_PANEL_BORDER);

        top.add(new JLabel("계좌:"));
        accountCombo = new JComboBox<>();
        accountCombo.setPreferredSize(UIConstants.ACCOUNT_COMBO_SIZE);
        accountCombo.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Account a) setText(a.getBank() + " / " + a.getAccountNumber());
                return this;
            }
        });
        accountCombo.addActionListener(e -> {
            Account sel = (Account) accountCombo.getSelectedItem();
            SessionManager.getInstance().setSelectedAccount(sel);
            rebuildList(sel);
        });
        top.add(accountCombo);


        balanceLabel = new JLabel("");
        balanceLabel.setFont(UIConstants.LARGE_FONT);
        top.add(balanceLabel);

        root.add(top, BorderLayout.NORTH);

        // 가운데: 스크롤 가능한 리스트
        listPanel = new JPanel();
        listPanel.setOpaque(false);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(UIConstants.LIST_PANEL_PADDING);

        scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        root.add(scroll, BorderLayout.CENTER);

        return root;
    }


    /** 최초 로딩: 계좌 채우기 + 선택 유지 */
    private void loadAccountsAndData() {
        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) return;

        List<Account> accounts = user.getAccountList();
        accountCombo.removeAllItems();
        for (Account a : accounts) accountCombo.addItem(a);

        Account selected = (initialSelection != null)
                ? initialSelection
                : SessionManager.getInstance().getSelectedAccount();
        if (selected == null && accountCombo.getItemCount() > 0) {
            selected = accountCombo.getItemAt(0);
            SessionManager.getInstance().setSelectedAccount(selected);
        }

        if (selected != null) {
            accountCombo.setSelectedItem(selected);
        }

        rebuildList((Account) accountCombo.getSelectedItem());
        syncSelectionFromSession();
    }

    /** 리스트 UI 재구성 (날짜 헤더 + 항목들) */
    private void rebuildList(Account account) {
        listPanel.removeAll();

        if (account == null) {
            balanceLabel.setText("");
            listPanel.revalidate(); listPanel.repaint();
            return;
        }

        // 상단 잔액
        balanceLabel.setText(UIConstants.won(account.getCurrentBalance()));

        List<Transaction> txs = account.getTransactionList();
        // 날짜별 그룹 (내림차순)
        Map<LocalDate, List<Transaction>> byDay = new TreeMap<>(Comparator.reverseOrder());
        for (Transaction t : txs) {
            LocalDate d = extractDate(t);
            byDay.computeIfAbsent(d, k -> new ArrayList<>()).add(t);
        }

        for (Map.Entry<LocalDate, List<Transaction>> e : byDay.entrySet()) {
            LocalDate day = e.getKey();
            List<Transaction> items = e.getValue();

            // 일별 합계(입금/출금)
            int sumIn = 0, sumOut = 0;
            for (Transaction t : items) {
                int amt = Math.abs(t.getAmount());
                if (t.getType() == TransactionType.INCOME) sumIn += amt;
                else sumOut += amt;
            }

            listPanel.add(makeDayHeader(day, sumIn, sumOut));
            listPanel.add(Box.createVerticalStrut(8));

            for (Transaction t : items) {
                TransactionItemRow rowComponent = new TransactionItemRow(account, t, extractDateTime(t));
                listPanel.add(rowComponent.createRow(() -> {
                    SessionManager.getInstance().setSelectedAccount(account);
                    SessionManager.getInstance().setSelectedTransaction(t);
                    SessionManager.getInstance().setPreviousRoute(Routes.TRANSACTION);
                    Router.getInstance().navigateUser(Routes.TRANSACTION_DETAIL);
                }));
                listPanel.add(Box.createVerticalStrut(6));
            }
            listPanel.add(Box.createVerticalStrut(10));
        }

        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }

    /** 날짜 헤더 + 일별 지출 합계) */
    private JComponent makeDayHeader(LocalDate day, int sumIn, int sumOut) {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(UIConstants.DAY_HEADER_PADDING);
        header.setPreferredSize(new Dimension(10, UIConstants.DAY_HEADER_HEIGHT));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, UIConstants.DAY_HEADER_HEIGHT));

        GridBagConstraints g = new GridBagConstraints();
        g.gridy = 0; g.insets = UIConstants.ZERO_INSETS;

        // 날짜
        JLabel dayLabel = new JLabel(day.format(HEADER_DOW));
        dayLabel.setFont(UIConstants.NORMAL_FONT);
        dayLabel.setForeground(UIConstants.TEXT_MUTED);
        g.gridx = 0; g.weightx = 1; g.anchor = GridBagConstraints.WEST;
        header.add(dayLabel, g);

        // 지출 합계
        int net = sumIn - sumOut;
        String sign = net >= 0 ? "+" : "-";
        int abs  = Math.abs(net);
        JLabel netLabel = new JLabel(sign + UIConstants.money(abs) + "원");
        netLabel.setFont(UIConstants.NORMAL_FONT);
        netLabel.setForeground(net > 0 ? UIConstants.POS_GREEN : Color.BLACK);
        netLabel.setToolTipText("입금 +" + UIConstants.money(sumIn) + "원 / 출금 -" + UIConstants.money(sumOut) + "원");

        g.gridx = 1; g.weightx = 0; g.anchor = GridBagConstraints.EAST;
        header.add(netLabel, g);

        return header;
    }



    // 날짜/시간 추출 유틸
    private LocalDate extractDate(Transaction t) {
        return extractDateTime(t).toLocalDate();
    }

    private LocalDateTime extractDateTime(Transaction t) {
        try {
            var m = t.getClass().getMethod("getDateTime");
            Object v = m.invoke(t);
            if (v instanceof LocalDateTime dt) return dt;
        } catch (Exception ignored) {}

        try {
            var m = t.getClass().getMethod("getDate");
            Object v = m.invoke(t);
            if (v != null) {
                String s = v.toString().trim();
                DateTimeFormatter[] fs = {
                        DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                };
                for (DateTimeFormatter f : fs) {
                    try { return LocalDateTime.parse(s, f); } catch (Exception ignore) {}
                }
                try {
                    var d = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                    return d.atStartOfDay();
                } catch (Exception ignore) {}
            }
        } catch (Exception ignored) {}

        return LocalDateTime.now();
    }

}
