package view.admin;

import model.*;
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

        String[] filters = {"지출 카테고리별", "구독 서비스별", "사용자별 지출 순위"};
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
        // 1. 전체 회원 가져오기
        List<User> allUsers = UserList.getInstance().getAll();
        Map<String, Integer> dataMap = new HashMap<>();

        for (User u : allUsers) {
            // 🔽 [수정] AccountService 없이 유저에게 직접 계좌 리스트 요청
            List<Account> accounts = u.getAccountList();

            for (Account acc : accounts) {
                // 🔽 [수정] AccountService 없이 계좌에게 직접 거래내역 요청
                List<Transaction> transactions = acc.getTransactionList();

                for (Transaction tx : transactions) {
                    // 지출만 통계에 포함
                    if (tx.getType() == TransactionType.EXPENSE) {
                        int amount = Math.abs(tx.getAmount());
                        String key = "";

                        if ("사용자별 지출 순위".equals(filterType)) {
                            key = u.getName() + " (" + u.getId() + ")";
                        }
                        else if ("지출 카테고리별".equals(filterType)) {
                            key = tx.getCategory();
                        }
                        else if ("구독 서비스별".equals(filterType)) {
                            if (isSubscription(tx.getCategory(), tx.getLocation())) {
                                key = tx.getLocation();
                            }
                        }

                        if (key != null && !key.isEmpty()) {
                            dataMap.put(key, dataMap.getOrDefault(key, 0) + amount);
                        }
                    }
                }
            }
        }
        chartPanel.setData(filterType, dataMap);
    }

    private boolean isSubscription(String category, String location) {
        if ("구독".equals(category)) return true;
        if (location == null) return false;
        String s = location.toLowerCase();
        return s.contains("넷플릭스") || s.contains("유튜브") || s.contains("멜론") ||
                s.contains("스포티파이") || s.contains("쿠팡") || s.contains("디즈니") ||
                s.contains("왓챠") || s.contains("티빙");
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
                g.drawString("집계된 데이터가 없습니다.", getWidth()/2 - 60, getHeight()/2);
                return;
            }

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 제목
            g2d.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            g2d.setColor(UIConstants.TEXT_PRIMARY_COLOR);
            g2d.drawString(title, 30, 40);

            // 1. 데이터 정렬 (내림차순)
            List<Map.Entry<String, Integer>> list = new ArrayList<>(data.entrySet());
            list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

            long total = list.stream().mapToLong(Map.Entry::getValue).sum();

            // 2. 상위 7개 + 기타로 재구성
            List<Map.Entry<String, Integer>> displayList = new ArrayList<>();
            if (list.size() > 8) {
                // 상위 7개 추가
                displayList.addAll(list.subList(0, 7));

                // 나머지 합쳐서 '기타'로 추가
                int otherTotal = 0;
                for (int i = 7; i < list.size(); i++) otherTotal += list.get(i).getValue();
                if (otherTotal > 0) {
                    displayList.add(new AbstractMap.SimpleEntry<>("기타", otherTotal));
                }
            } else {
                displayList.addAll(list);
            }

            // 3. 차트 그리기
            int diameter = Math.min(getWidth()/2, getHeight() - 100);
            int startAngle = 90;
            int chartX = 50, chartY = (getHeight() - diameter)/2 + 20;
            int legendX = chartX + diameter + 60, legendY = chartY;
            int currentAngleSum = 0;

            g2d.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

            for (int i = 0; i < displayList.size(); i++) {
                Map.Entry<String, Integer> entry = displayList.get(i);
                int value = entry.getValue();

                // ★ [수정] 각도 계산: 비율대로 정확하게 계산
                int angle = (int) Math.round((double) value / total * 360);

                // 마지막 조각에서만 360도 보정 (1~2도 오차 해결용)
                if (i == displayList.size() - 1) {
                    angle = 360 - currentAngleSum;
                }

                // 파이 그리기
                g2d.setColor(colors[i % colors.length]);
                g2d.fillArc(chartX, chartY, diameter, diameter, startAngle, angle);

                // 범례 그리기
                if (legendY + 25 < getHeight()) {
                    g2d.fillRect(legendX, legendY, 15, 15);
                    g2d.setColor(Color.BLACK);
                    double percent = (double) value / total * 100;
                    // 텍스트 포맷: 항목명 (금액, %)
                    String label = String.format("%s : %,d원 (%.1f%%)",
                            entry.getKey(), value, percent);
                    g2d.drawString(label, legendX + 25, legendY + 12);
                    legendY += 28;
                }

                startAngle += angle;
                currentAngleSum += angle;
            }

            // 도넛 구멍
            g2d.setColor(Color.WHITE);
            int hole = diameter / 2;
            g2d.fillOval(chartX + hole/2, chartY + hole/2, hole, hole);

            // 총합 표시
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("맑은 고딕", Font.BOLD, 14));
            String totalStr = String.format("Total: %,d원", total);
            FontMetrics fm = g2d.getFontMetrics();
            int tx = chartX + hole/2 + (hole - fm.stringWidth(totalStr))/2;
            int ty = chartY + hole/2 + (hole + fm.getAscent())/2 - 5;
            g2d.drawString(totalStr, tx, ty);
        }
    }
}
