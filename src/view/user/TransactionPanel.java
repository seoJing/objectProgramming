package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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

public class TransactionPanel extends UserLayout {

    private JComboBox<Account> accountCombo;
    private JLabel balanceLabel;

    private JPanel listPanel;           // 리스트 컨테이너 (Y축)
    private JScrollPane scroll;

    private Account initialSelection;

    private static final DateTimeFormatter HHMM= DateTimeFormatter.ofPattern("HH:mm");
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

        // CardLayout으로 재표시될 때마다 선택 계좌를 번호로 강제 적용
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentShown(java.awt.event.ComponentEvent e) {
                syncSelectionFromSession();   // 아래 메서드
            }
        });
    }

    // 세션에 저장된 계좌로 콤보/목록 동기화
    private void syncSelectionFromSession() {
        Account s = SessionManager.getInstance().getSelectedAccount();
        if (s == null) return;
        selectByAccountNumber(s.getAccountNumber());            // 번호로만 매칭
        rebuildList((Account) accountCombo.getSelectedItem());  // 화면 갱신
    }

    // 계좌번호로만 콤보에서 선택
    private void selectByAccountNumber(String accNo) {
        if (accNo == null) return;
        for (int i = 0; i < accountCombo.getItemCount(); i++) {
            Account a = accountCombo.getItemAt(i);
            if (accNo.equals(a.getAccountNumber())) {
                accountCombo.setSelectedIndex(i);
                SessionManager.getInstance().setSelectedAccount(a); // 세션도 맞춤
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
        scroll.getVerticalScrollBar().setUnitIncrement(16); // 스크롤 속도
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

        // 1순위: 생성자 전달값 → 2순위: 세션값 → 3순위: 첫 계좌
        Account selected = (initialSelection != null)
                ? initialSelection
                : SessionManager.getInstance().getSelectedAccount();
        if (selected == null && accountCombo.getItemCount() > 0) {
            selected = accountCombo.getItemAt(0);
            SessionManager.getInstance().setSelectedAccount(selected);
        }

        if (selected != null) {
            accountCombo.setSelectedItem(selected); // equals로 매칭
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
                listPanel.add(makeItemRow(account, t));
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
        // 참고용 툴팁(원하면 유지/삭제)
        netLabel.setToolTipText("입금 +" + UIConstants.money(sumIn) + "원 / 출금 -" + UIConstants.money(sumOut) + "원");

        g.gridx = 1; g.weightx = 0; g.anchor = GridBagConstraints.EAST;
        header.add(netLabel, g);

        return header;
    }


    /** 거래 내역 */
    private JComponent makeItemRow(Account account, Transaction t) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(true);
        row.setBackground(UIConstants.TX_ROW_BG);
        row.setBorder(UIConstants.TX_ROW_BORDER);

        // 클릭
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // 이 행이 어떤 거래인지 바인딩
        row.putClientProperty("account", account);
        row.putClientProperty("tx", t);

        // 클릭 시 상세로 이동
        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                Account a = (Account)((JComponent)e.getSource()).getClientProperty("account");
                Transaction tx = (Transaction)((JComponent)e.getSource()).getClientProperty("tx");
                SessionManager.getInstance().setSelectedAccount(a);
                SessionManager.getInstance().setSelectedTransaction(tx);
                SessionManager.getInstance().setFromAllTransactions(false);
                Router.getInstance().navigateUser(Routes.TRANSACTION_DETAIL);
            }
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                row.setBackground(row.getBackground().darker());
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                row.setBackground(UIConstants.TX_ROW_BG);
            }
        });

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.insets = UIConstants.ZERO_INSETS;

        // 아이콘
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));


        JLabel icon = new JLabel(loadIcon("/resources/images/bag.png", 22, 22));

        left.add(Box.createHorizontalStrut(6));
        left.add(icon);
        left.setBorder(UIConstants.ICON_LEFT_PADDING); // 아이콘

        gc.gridx = 0;
        gc.weightx = 0;
        gc.anchor  = GridBagConstraints.WEST;
        row.add(left, gc);

        // 날짜, 시간
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(safe(t.getLocation()));
        title.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));

        String hhmm = HHMM.format(extractDateTime(t));
        JLabel time = new JLabel(hhmm);
        time.setFont(UIConstants.SMALL_FONT);
        time.setForeground(UIConstants.TEXT_MUTED);

        String sub = safe(t.getCategory()) + "  |  " + account.getBank();
        JLabel subLabel = new JLabel(sub);
        subLabel.setFont(UIConstants.SMALL_FONT);
        subLabel.setForeground(UIConstants.TEXT_MUTED);

        center.add(title);
        center.add(Box.createVerticalStrut(2));
        center.add(time);           // ← 시간은 날짜 아래 줄로
        center.add(Box.createVerticalStrut(4));
        center.add(subLabel);

        gc.gridx   = 1;
        gc.weightx = 1;
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.anchor  = GridBagConstraints.WEST;
        row.add(center, gc);

        // 금액 (+초록 / −검정), 우측 끝 정렬
        boolean deposit = (t.getType() == TransactionType.INCOME);
        String sign = deposit ? "+" : "-";
        JLabel amount = new JLabel(sign + UIConstants.money(t.getAmount()) + "원");
        amount.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));
        amount.setForeground(deposit ? UIConstants.POS_GREEN : Color.BLACK);

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.add(amount, BorderLayout.EAST);   // 우측 끝에 딱 붙이기

        gc.gridx   = 2;
        gc.weightx = 0;
        gc.anchor  = GridBagConstraints.EAST;
        gc.insets  = UIConstants.RIGHT_GAP_M;   // 가운데와의 간격
        row.add(right, gc);

        return row;
    }
    // 날짜/시간 추출 유틸
    private LocalDate extractDate(Transaction t) {
        return extractDateTime(t).toLocalDate();
    }

    private LocalDateTime extractDateTime(Transaction t) {
        // 1) getDateTime(): LocalDateTime 우선
        try {
            var m = t.getClass().getMethod("getDateTime");
            Object v = m.invoke(t);
            if (v instanceof LocalDateTime dt) return dt;
        } catch (Exception ignored) {}

        // 2) getDate(): String → 가능한 형식 파싱
        try {
            var m = t.getClass().getMethod("getDate");
            Object v = m.invoke(t);
            if (v != null) {
                String s = v.toString().trim();
                // 가벼운 포맷 후보들
                DateTimeFormatter[] fs = {
                        DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                };
                for (DateTimeFormatter f : fs) {
                    try { return LocalDateTime.parse(s, f); } catch (Exception ignore) {}
                }
                // 시간 없으면 자정으로
                try {
                    var d = LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                    return d.atStartOfDay();
                } catch (Exception ignore) {}
            }
        } catch (Exception ignored) {}

        return LocalDateTime.now();
    }

    private static String safe(String s) { return (s == null) ? "" : s; }

    // 간단 아이콘 로더(없으면 투명)
    private static Icon loadIcon(String path, int w, int h) {
        try {
            var url = Objects.requireNonNull(TransactionPanel.class.getResource(path));
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            Image empty = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            return new ImageIcon(empty);
        }
    }
}
