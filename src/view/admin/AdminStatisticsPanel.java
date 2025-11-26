package view.admin;

import model.*;
import service.AccountService;
import util.UIConstants;
import view.layout.AdminLayout;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AdminStatisticsPanel extends AdminLayout {

    private JComboBox<String> filterCombo;
    private ChartPanel chartPanel;

    public AdminStatisticsPanel() {
        super();
        setContent(createContent());
        // 초기값: 지출 카테고리별 통계 로드
        updateChart("지출 카테고리별");
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // 1. 상단 필터링 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);

        JLabel label = new JLabel("통계 기준 선택: ");
        label.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));

        String[] filters = {"지출 카테고리별", "구독 서비스별", "은행/카드사별"};
        filterCombo = new JComboBox<>(filters);
        filterCombo.setPreferredSize(new Dimension(200, 32));
        filterCombo.setFont(UIConstants.NORMAL_FONT);
        filterCombo.addActionListener(e -> {
            String selected = (String) filterCombo.getSelectedItem();
            updateChart(selected);
        });

        topPanel.add(label);
        topPanel.add(filterCombo);

        panel.add(topPanel, BorderLayout.NORTH);

        // 2. 중앙 차트 패널
        chartPanel = new ChartPanel();
        panel.add(chartPanel, BorderLayout.CENTER);

        return panel;
    }

    private void updateChart(String filterType) {
        List<User> allUsers = UserList.getInstance().getAll();
        Map<String, Integer> dataMap = new HashMap<>();

        for (User u : allUsers) {
            List<Account> accounts = AccountService.getInstance().getAccounts(u);
            for (Account acc : accounts) {

                if ("은행/카드사별".equals(filterType)) {
                    // 은행별 지출 건수 집계
                    for (Transaction tx : AccountService.getInstance().getTransactions(acc)) {
                        if (tx.getType() == TransactionType.EXPENSE) {
                            String bank = acc.getBank();
                            dataMap.put(bank, dataMap.getOrDefault(bank, 0) + 1);
                        }
                    }
                } else {
                    for (Transaction tx : AccountService.getInstance().getTransactions(acc)) {
                        if (tx.getType() == TransactionType.EXPENSE) {
                            String cat = tx.getCategory();
                            String loc = tx.getLocation();
                            int amount = Math.abs(tx.getAmount()); // 금액 기준

                            if ("지출 카테고리별".equals(filterType)) {
                                if (cat != null && !cat.isEmpty()) {
                                    dataMap.put(cat, dataMap.getOrDefault(cat, 0) + amount);
                                }
                            } else if ("구독 서비스별".equals(filterType)) {
                                if (isSubscription(cat, loc)) {
                                    dataMap.put(loc, dataMap.getOrDefault(loc, 0) + amount);
                                }
                            }
                        }
                    }
                }
            }
        }
        chartPanel.setData(filterType + " (전체 회원 기준)", dataMap);
    }

    private boolean isSubscription(String category, String location) {
        if ("구독".equals(category)) return true;
        if (location == null) return false;
        String s = location.toLowerCase();
        return s.contains("넷플릭스") || s.contains("유튜브") || s.contains("멜론") ||
                s.contains("스포티파이") || s.contains("쿠팡") || s.contains("디즈니");
    }

    // 내부 클래스: 원형 차트 패널
    private static class ChartPanel extends JPanel {
        private String title = "";
        private Map<String, Integer> data = new HashMap<>();
        private final Color[] colors = {
                new Color(255, 99, 132), new Color(54, 162, 235), new Color(255, 206, 86),
                new Color(75, 192, 192), new Color(153, 102, 255), new Color(255, 159, 64),
                new Color(201, 203, 207), new Color(46, 204, 113)
        };

        public ChartPanel() { setBackground(Color.WHITE); }

        public void setData(String title, Map<String, Integer> newData) {
            this.title = title;
            this.data = newData;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.isEmpty()) {
                g.setColor(Color.GRAY);
                g.setFont(new Font("맑은 고딕", Font.BOLD, 16));
                g.drawString("집계된 데이터가 없습니다.", getWidth()/2 - 80, getHeight()/2);
                return;
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            g2d.setColor(UIConstants.TEXT_PRIMARY_COLOR);
            g2d.drawString(title, 30, 40);

            List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(data.entrySet());
            sortedList.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

            long total = data.values().stream().mapToLong(Integer::intValue).sum();

            int diameter = Math.min(getWidth()/2, getHeight() - 100);
            int startAngle = 90;
            int chartX = 50, chartY = (getHeight() - diameter)/2 + 20;
            int legendX = chartX + diameter + 50, legendY = chartY;

            g2d.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

            int currentAngleSum = 0;
            for (int i = 0; i < sortedList.size(); i++) {
                if (i >= 8) break;
                Map.Entry<String, Integer> entry = sortedList.get(i);
                int arcAngle;
                if (i == sortedList.size() - 1 || i == 7) {
                    arcAngle = 360 - currentAngleSum;
                } else {
                    arcAngle = (int) Math.round((double) entry.getValue() / total * 360);
                }

                g2d.setColor(colors[i % colors.length]);
                g2d.fillArc(chartX, chartY, diameter, diameter, startAngle, arcAngle);

                if (legendY + 30 < getHeight()) {
                    g2d.fillRect(legendX, legendY, 15, 15);
                    g2d.setColor(Color.BLACK);
                    String label = String.format("%s : %,d (%.1f%%)", entry.getKey(), entry.getValue(), (double)entry.getValue()/total*100);
                    g2d.drawString(label, legendX + 25, legendY + 13);
                    legendY += 30;
                }
                startAngle += arcAngle;
                currentAngleSum += arcAngle;
            }

            g2d.setColor(Color.WHITE);
            int hole = diameter / 2;
            g2d.fillOval(chartX + hole/2, chartY + hole/2, hole, hole);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("맑은 고딕", Font.BOLD, 16));
            String totalStr = String.format("Total: %,d", total);
            FontMetrics fm = g2d.getFontMetrics();
            int tx = chartX + hole/2 + (hole - fm.stringWidth(totalStr))/2;
            int ty = chartY + hole/2 + (hole + fm.getAscent())/2 - 5;
            g2d.drawString(totalStr, tx, ty);
        }
    }
}
