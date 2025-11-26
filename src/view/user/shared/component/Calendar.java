package view.user.shared.component;

import util.UIConstants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.function.Consumer;

public class Calendar extends JPanel {
    private YearMonth ym;
    private final Consumer<LocalDate> onPick;
    private final int cellSize;
    private final int gridGap;
    private final Insets padding;

    private final JLabel title = new JLabel("", SwingConstants.CENTER);
    private final JPanel grid;

    public Calendar(LocalDate initial, Consumer<LocalDate> onPick) {
        this(initial, onPick, 44, 8, new Insets(12, 12, 12, 12));
    }

    public Calendar(LocalDate initial, Consumer<LocalDate> onPick,
                    int cellSize, int gridGap, Insets padding) {
        this.onPick = onPick;
        this.cellSize = cellSize;
        this.gridGap = gridGap;
        this.padding = padding;

        this.ym = YearMonth.from(initial);

        setLayout(new BorderLayout(8, 8));
        setOpaque(true);
        setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        setBorder(UIConstants.TOP_PANEL_CAL_BORDER);

        setBorder(new EmptyBorder(this.padding));

        // 그리드 패널 준비
        this.grid = createGridPanel();

        // ==== 헤더(월 이동) ====
        JPanel head = new JPanel(new BorderLayout());
        head.setOpaque(false);

        JButton prev = new JButton("◀");
        JButton next = new JButton("▶");

        prev.addActionListener(e -> {
            ym = ym.minusMonths(1);
            rebuild();
        });
        next.addActionListener(e -> {
            ym = ym.plusMonths(1);
            rebuild();
        });

        title.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));
        head.add(prev, BorderLayout.WEST);
        head.add(title, BorderLayout.CENTER);
        head.add(next, BorderLayout.EAST);

        // ==== 요일 헤더 + 날짜 그리드 ====
        JPanel box = new JPanel(new BorderLayout(0, 4));
        box.setOpaque(false);

        JPanel dow = new JPanel(new GridLayout(1, 7));
        dow.setOpaque(false);
        String[] names = {"월", "화", "수", "목", "금", "토", "일"};
        for (String n : names) {
            JLabel l = new JLabel(n, SwingConstants.CENTER);
            l.setFont(UIConstants.SMALL_FONT);
            l.setForeground(UIConstants.TEXT_MUTED);
            dow.add(l);
        }

        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(4, 0, 0, 0));

        box.add(dow, BorderLayout.NORTH);
        box.add(grid, BorderLayout.CENTER);

        add(head, BorderLayout.NORTH);
        add(box, BorderLayout.CENTER);

        rebuild();
    }

    private void rebuild() {
        title.setText(ym.getYear() + "년 " + ym.getMonthValue() + "월");
        grid.removeAll();

        LocalDate first = ym.atDay(1);
        int lead = (first.getDayOfWeek() == DayOfWeek.SUNDAY
                ? 7
                : first.getDayOfWeek().getValue()) - 1;

        // 앞쪽 빈칸
        for (int i = 0; i < lead; i++) {
            grid.add(new JLabel(""));
        }

        int len = ym.lengthOfMonth();
        for (int d = 1; d <= len; d++) {
            LocalDate date = ym.atDay(d);
            JButton b = makeDayButton(d, date);
            grid.add(b);
        }

        // 뒤쪽 패딩
        int cells = lead + len;
        int pad = (7 - (cells % 7)) % 7;
        for (int i = 0; i < pad; i++) {
            grid.add(new JLabel(""));
        }

        grid.revalidate();
        grid.repaint();
    }

    private JButton makeDayButton(int day, LocalDate date) {
        JButton b = new JButton(String.valueOf(day));
        b.setFocusPainted(false);
        b.setFont(UIConstants.NORMAL_FONT);
        b.setMargin(UIConstants.ZERO_INSETS);

        Dimension d = new Dimension(cellSize, cellSize);
        b.setPreferredSize(d);
        b.setMinimumSize(d);
        b.setMaximumSize(d);

        // 💡 여기 “한 번만” onPick 호출
        b.addActionListener(e -> onPick.accept(date));

        // 오늘 날짜 강조
        LocalDate today = LocalDate.now();
        if (date.equals(today)) {
            b.setForeground(UIConstants.USER_HEADER_COLOR);
            b.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));
        }

        return b;
    }

    private JPanel createGridPanel() {
        JPanel grid = new JPanel(new GridLayout(0, 7, gridGap, gridGap));
        grid.setOpaque(false);
        return grid;
    }
}
