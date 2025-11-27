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
import java.awt.Insets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import model.Account;
import model.Transaction;
import model.TransactionType;
import model.User;
import util.Router;
import util.Routes;
import util.SessionManager;
import util.UIConstants;
import view.layout.UserLayout;
import view.user.shared.component.Calendar;

public class AllTransactionsPanel extends UserLayout {

    private JPanel listPanel;
    private JScrollPane scroll;
    private final NavigableMap<LocalDate, JComponent> dayHeaderMap = new TreeMap<>();
    private JPanel calendarWrapper;
    private Calendar miniCalendar;
    private boolean calendarVisible = true;
    private TransactionType typeFilter = null;
    private String categoryFilter = null;

    // 타입(전체/입금/출금) 순환 버튼
    private JButton typeCycleBtn;
    private final String[] TYPE_LABELS = {"전체", "입금", "출금"};
    private final TransactionType[] TYPE_VALUES = {
            null,
            TransactionType.INCOME,
            TransactionType.EXPENSE
    };
    private int typeIndex = 0; // 0: 전체, 1: 입금만, 2: 출금만

    // 카테고리 드롭다운
    private JComboBox<String> categoryCombo;
    private final String[] CATEGORIES = {
            "전체 카테고리",
            "식비", "생활", "구독", "간식", "카페", "교통", "의료", "문화"
    };

    // ====== 포맷터 ======
    private static final DateTimeFormatter HHMM =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter HEADER_DOW =
            DateTimeFormatter.ofPattern("d일 E요일", Locale.KOREAN);

    public AllTransactionsPanel() {
        super();
        setContent(createContent());
        loadAndRender();
    }

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.PANEL_BG);

        // -------------------- 상단 영역 (제목 + 필터 + 캘린더) --------------------
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        top.setBorder(UIConstants.TOP_PANEL_CAL_BORDER);

        // 1) 제목 + 캘린더 토글 버튼 행
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setOpaque(false);

        JLabel title = new JLabel("가계부");
        title.setFont(UIConstants.TITLE_FONT);
        title.setForeground(UIConstants.TEXT_DEFAULT);
        headerBar.add(title, BorderLayout.WEST);

        JButton toggleCalBtn = new JButton("캘린더 접기");
        toggleCalBtn.setFont(UIConstants.SMALL_FONT);
        toggleCalBtn.setOpaque(true);
        toggleCalBtn.setBorder(UIConstants.TX_ROW_BORDER);
        toggleCalBtn.setBackground(Color.WHITE);
        toggleCalBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleCalBtn.addActionListener(e -> toggleCalendarVisibility(toggleCalBtn));
        headerBar.add(toggleCalBtn, BorderLayout.EAST);

        top.add(headerBar, BorderLayout.NORTH);

        // 2) 필터 바 (타입 순환 버튼 + 카테고리 콤보)
        top.add(createFilterBar(), BorderLayout.CENTER);

        // 3) 미니 캘린더
        LocalDate initialDate =
                (SessionManager.getInstance().getSelectedDate() != null)
                        ? SessionManager.getInstance().getSelectedDate()
                        : LocalDate.now();

        miniCalendar = new Calendar(
                initialDate,
                picked -> {
                    SessionManager.getInstance().setSelectedDate(picked);
                    scrollToDate(picked);
                }
        );

        calendarWrapper = new JPanel(new BorderLayout());
        calendarWrapper.setOpaque(false);
        calendarWrapper.add(miniCalendar, BorderLayout.CENTER);

        top.add(calendarWrapper, BorderLayout.SOUTH);

        root.add(top, BorderLayout.NORTH);

        // -------------------- 리스트 영역 --------------------
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

    // 필터링 UI
    private JComponent createFilterBar() {
        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(12, 0, 8, 0));

        // ---------- 왼쪽: 타입(전체/입금만/출금만) 순환 버튼 ----------
        typeCycleBtn = new JButton();
        typeCycleBtn.setFocusPainted(false);
        typeCycleBtn.setFont(UIConstants.NORMAL_FONT);
        typeCycleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        typeCycleBtn.setMargin(new Insets(6, 18, 6, 18));
        typeCycleBtn.setBackground(Color.WHITE);
        typeCycleBtn.setContentAreaFilled(true);
        typeCycleBtn.setOpaque(false);

        applyTypeFromIndex();

        typeCycleBtn.addActionListener(e -> {
            typeIndex = (typeIndex + 1) % TYPE_LABELS.length;
            applyTypeFromIndex();
            loadAndRender();
        });


        JPanel leftWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftWrap.setOpaque(false);
        leftWrap.add(typeCycleBtn);
        root.add(leftWrap, BorderLayout.WEST);

        // ---------- 오른쪽: 카테고리 드롭다운 ----------
        categoryCombo = new JComboBox<>(CATEGORIES);
        categoryCombo.setFont(UIConstants.NORMAL_FONT);
        categoryCombo.setSelectedIndex(0);
        categoryCombo.setBorder(BorderFactory.createEmptyBorder());

        categoryCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                l.setHorizontalAlignment(SwingConstants.LEFT);
                l.setBorder(new EmptyBorder(6, 12, 6, 12));
                return l;
            }
        });

        categoryCombo.setBackground(Color.WHITE);

        categoryCombo.addActionListener(e -> {
            int idx = categoryCombo.getSelectedIndex();
            if (idx <= 0) {
                categoryFilter = null;
            } else {
                categoryFilter = CATEGORIES[idx];
            }
            loadAndRender();
        });

        JPanel rightWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightWrap.setOpaque(false);
        rightWrap.add(categoryCombo);
        root.add(rightWrap, BorderLayout.CENTER);

        return root;
    }

    private void applyTypeFromIndex() {
        typeCycleBtn.setText(TYPE_LABELS[typeIndex]);
        typeFilter = TYPE_VALUES[typeIndex];

        switch (typeIndex) {
            case 0 -> { // 전체
                typeCycleBtn.setBackground(UIConstants.TEXT_MUTED);
            }
            case 1 -> { // 입금만
                typeCycleBtn.setBackground(UIConstants.ACCENT_COLOR);
            }
            case 2 -> { // 출금만
                typeCycleBtn.setBackground(UIConstants.ADMIN_HEADER_COLOR);
            }
        }
    }

    // 캘린더 접기/펼치기
    private void toggleCalendarVisibility(JButton btn) {
        calendarVisible = !calendarVisible;

        if (calendarWrapper != null) {
            calendarWrapper.setVisible(calendarVisible);
        }

        btn.setText(calendarVisible ? "캘린더 접기" : "캘린더 펼치기");

        revalidate();
        repaint();
    }

    private void loadAndRender() {
        listPanel.removeAll();
        dayHeaderMap.clear();

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            listPanel.add(new JLabel("로그인이 필요합니다."));
            revalidateRepaint();
            return;
        }

        List<Account> accounts = user.getAccountList();

        // 1) 계좌별 거래를 날짜(LocalDate)로 그룹 (내림차순 정렬)
        Map<LocalDate, List<Entry>> byDay = new TreeMap<>(Comparator.reverseOrder());
        int filteredCount = 0;
        for (Account acc : accounts) {
            for (Transaction t : acc.getTransactionList()) {

                if (!passesFilter(t)) continue;

                filteredCount++;
                LocalDateTime dt = extractDateTime(t);
                byDay.computeIfAbsent(dt.toLocalDate(), k -> new ArrayList<>())
                        .add(new Entry(acc, t, dt));
            }
        }
        System.out.println("✓ 거래 필터링 완료: " + filteredCount + "개 항목");

        // 2) 날짜별로 헤더 + 행 렌더링
        for (Map.Entry<LocalDate, List<Entry>> e : byDay.entrySet()) {
            LocalDate day = e.getKey();
            List<Entry> items = e.getValue();

            items.sort(Comparator.comparing((Entry en) -> en.when).reversed());

            int sumIn = 0, sumOut = 0;
            for (Entry en : items) {
                int amt = Math.abs(en.tx.getAmount());
                if (en.tx.getType() == TransactionType.INCOME) sumIn += amt;
                else sumOut += amt;
            }

            JComponent header = makeDayHeader(day, sumIn, sumOut);
            listPanel.add(header);
            dayHeaderMap.put(day, header);

            listPanel.add(Box.createVerticalStrut(8));

            for (Entry en : items) {
                listPanel.add(makeRow(en.acc, en.tx, en.when));
                listPanel.add(Box.createVerticalStrut(6));
            }
            listPanel.add(Box.createVerticalStrut(10));
        }

        listPanel.add(Box.createVerticalGlue());
        revalidateRepaint();

        LocalDate selected = SessionManager.getInstance().getSelectedDate();
        if (selected != null) {
            scrollToDate(selected);
        }
    }

    private boolean passesFilter(Transaction t) {
        if (typeFilter != null && t.getType() != typeFilter) {
            return false;
        }
        if (categoryFilter != null && !categoryFilter.isBlank()) {
            String cat = t.getCategory();
            if (cat == null || !cat.equals(categoryFilter)) {
                return false;
            }
        }
        return true;
    }

    private void revalidateRepaint() {
        listPanel.revalidate();
        listPanel.repaint();
    }

    private void scrollToDate(LocalDate day) {
        if (day == null || dayHeaderMap.isEmpty()) return;

        LocalDate target = day;

        if (!dayHeaderMap.containsKey(day)) {
            var floor = dayHeaderMap.floorEntry(day);
            if (floor != null) {
                target = floor.getKey();
            } else {
                target = dayHeaderMap.firstKey();
            }
        }

        JComponent header = dayHeaderMap.get(target);
        if (header == null) return;

        SwingUtilities.invokeLater(() -> {
            int y = header.getY();
            int targetY = Math.max(y - 8, 0);
            JScrollBar bar = scroll.getVerticalScrollBar();
            bar.setValue(targetY);
        });
    }

    private static class Entry {
        final Account acc;
        final Transaction tx;
        final LocalDateTime when;

        Entry(Account acc, Transaction tx, LocalDateTime when) {
            this.acc = acc;
            this.tx = tx;
            this.when = when;
        }
    }

    private JComponent makeDayHeader(LocalDate day, int sumIn, int sumOut) {
        JPanel header = new JPanel(new GridBagLayout());
        header.setOpaque(false);
        header.setBorder(UIConstants.DAY_HEADER_PADDING);
        header.setPreferredSize(new Dimension(10, UIConstants.DAY_HEADER_HEIGHT));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, UIConstants.DAY_HEADER_HEIGHT));

        GridBagConstraints g = new GridBagConstraints();
        g.gridy = 0;
        g.insets = UIConstants.ZERO_INSETS;

        JLabel dayLabel = new JLabel(day.format(HEADER_DOW));
        dayLabel.setFont(UIConstants.NORMAL_FONT);
        dayLabel.setForeground(UIConstants.TEXT_MUTED);

        g.gridx = 0;
        g.weightx = 1;
        g.anchor = GridBagConstraints.WEST;
        header.add(dayLabel, g);

        int net = sumIn - sumOut;
        String sign = net >= 0 ? "+" : "-";
        int abs = Math.abs(net);

        JLabel netLabel = new JLabel(sign + UIConstants.money(abs) + "원");
        netLabel.setFont(UIConstants.NORMAL_FONT);
        netLabel.setForeground(net > 0 ? UIConstants.POS_GREEN : UIConstants.TEXT_DEFAULT);
        netLabel.setToolTipText("입금 +" + UIConstants.money(sumIn) + "원 / 출금 -" + UIConstants.money(sumOut) + "원");

        g.gridx = 1;
        g.weightx = 0;
        g.anchor = GridBagConstraints.EAST;
        header.add(netLabel, g);

        return header;
    }

    private JComponent makeRow(Account account, Transaction t, LocalDateTime dt) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setOpaque(true);
        row.setBackground(UIConstants.TX_ROW_BG);
        row.setBorder(UIConstants.TX_ROW_BORDER);
        row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        row.putClientProperty("account", account);
        row.putClientProperty("tx", t);

        row.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Account a = (Account) ((JComponent) e.getSource()).getClientProperty("account");
                Transaction tx = (Transaction) ((JComponent) e.getSource()).getClientProperty("tx");
                SessionManager.getInstance().setSelectedAccount(a);
                SessionManager.getInstance().setSelectedTransaction(tx);
                SessionManager.getInstance().setPreviousRoute(Routes.ALL_TRANSACTIONS);
                Router.getInstance().navigateUser(Routes.TRANSACTION_DETAIL);
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

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.add(Box.createHorizontalStrut(6));
        left.setBorder(UIConstants.ICON_LEFT_PADDING);

        gc.gridx = 0;
        gc.weightx = 0;
        gc.anchor = GridBagConstraints.WEST;
        row.add(left, gc);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JLabel title = new JLabel(safe(t.getLocation()));
        title.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));

        JLabel time = new JLabel(HHMM.format(dt));
        time.setFont(UIConstants.SMALL_FONT);
        time.setForeground(UIConstants.TEXT_MUTED);

        String sub = safe(t.getCategory()) + "  |  " +
                account.getBank() + " / " + account.getAccountNumber();
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

        boolean deposit = (t.getType() == TransactionType.INCOME);
        String sign = deposit ? "+" : "-";
        JLabel amount = new JLabel(sign + UIConstants.money(t.getAmount()) + "원");
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

    private static LocalDateTime extractDateTime(Transaction t) {
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

    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}
