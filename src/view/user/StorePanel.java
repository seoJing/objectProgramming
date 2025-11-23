package view.user;

import static util.UIConstants.BACKGROUND_BUTTON;
import static util.UIConstants.BACKGROUND_LIGHT;
import static util.UIConstants.BUTTON_HORIZONTAL_GAP;
import static util.UIConstants.NORMAL_FONT;
import static util.UIConstants.POS_GREEN;
import static util.UIConstants.SMALL_FONT;
import static util.UIConstants.TEXT_PRIMARY_COLOR;
import static util.UIConstants.WHITE;
import static util.UIConstants.won;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import util.Router;
import util.Routes;
import view.layout.UserLayout;

/**
 * 내 구독 - 장바구니 / 카테고리별 구독 리스트 / 총액 화면
 */
public class StorePanel extends UserLayout {

    // ✅ 현재 생성된 StorePanel 인스턴스 저장
    private static StorePanel instance;

    // ✅ 카테고리별 구독 저장용 (문자열 대신 객체로)
    private static class SubscriptionItem {
        String display; // "YouTube (그룹핑) - 4,975원/월"
        int price;      // 4975

        SubscriptionItem(String display, int price) {
            this.display = display;
            this.price = price;
        }
    }

    private final Map<String, List<SubscriptionItem>> categorySubs = new HashMap<>();

    // 총 금액
    private int totalPrice = 0;
    private JLabel summaryText;

    // ✅ 구독 리스트를 그릴 패널 (스크롤 안쪽)
    private JPanel subsListPanel;

    public StorePanel() {
        super();
        instance = this;   // ✅ 생성 시 자기 자신 저장
        setContent(createContent());
    }

    // ✅ 다른 화면에서 StorePanel 사용 시
    public static StorePanel getInstance() {
        return instance;
    }

    /* ====================== UI 구성 ====================== */

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND_LIGHT);

        // 가운데 메인 컨텐츠
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // 타이틀
        JLabel basketLabel = new JLabel("구독 장바구니", SwingConstants.CENTER);
        basketLabel.setFont(SMALL_FONT.deriveFont(Font.PLAIN, 13f));
        basketLabel.setForeground(TEXT_PRIMARY_COLOR);
        basketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(basketLabel);
        centerPanel.add(Box.createVerticalStrut(8));

        // ===== 장바구니 / 그룹핑 버튼 줄 =====
        JPanel modePanel = new JPanel(new GridLayout(1, 2, BUTTON_HORIZONTAL_GAP, 0));
        modePanel.setOpaque(false);

        JButton basketBtn = new JButton("장바구니");
        JButton groupBtn  = new JButton("그룹핑");
        styleFlatButton(basketBtn);
        styleFlatButton(groupBtn);

        modePanel.add(basketBtn);
        modePanel.add(groupBtn);
        centerPanel.add(modePanel);
        centerPanel.add(Box.createVerticalStrut(16));

        // ===== 카테고리별 구독 리스트 =====
        JPanel listOuter = new JPanel(new BorderLayout());
        listOuter.setBackground(BACKGROUND_BUTTON);
        listOuter.setPreferredSize(new Dimension(0, 260));

        // 제목 + [구독 추가] 버튼을 담는 헤더 패널
        JPanel listHeader = new JPanel(new BorderLayout());
        listHeader.setOpaque(false);

        JLabel listTitle = new JLabel("카테고리별 구독 리스트");
        listTitle.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        listTitle.setFont(NORMAL_FONT.deriveFont(Font.BOLD, 13f));
        listTitle.setForeground(TEXT_PRIMARY_COLOR);

        JButton addSubButton = new JButton("구독 추가") {

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int arc = 35;

                g2.setColor(WHITE);
                g2.fillRoundRect(0, 0, width - 3, height - 3, arc, arc);

                super.paintComponent(g);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int arc = 35;

                g2.setColor(POS_GREEN);
                g2.drawRoundRect(0, 0, width - 3, height - 3, arc, arc);

                g2.dispose();
            }
        };

        addSubButton.setContentAreaFilled(false);
        addSubButton.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));

        listHeader.add(listTitle, BorderLayout.WEST);
        listHeader.add(addSubButton, BorderLayout.EAST);

        listOuter.add(listHeader, BorderLayout.NORTH);

        // ✅ 리스트가 들어갈 패널 (각 항목 + 취소 버튼)
        subsListPanel = new JPanel();
        subsListPanel.setOpaque(false);
        subsListPanel.setLayout(new BoxLayout(subsListPanel, BoxLayout.Y_AXIS));

        JScrollPane subsScroll = new JScrollPane(subsListPanel);
        subsScroll.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        subsScroll.getVerticalScrollBar().setUnitIncrement(16);
        listOuter.add(subsScroll, BorderLayout.CENTER);

        centerPanel.add(listOuter);
        centerPanel.add(Box.createVerticalStrut(16));

        // ===== 총액 패널 =====
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(BACKGROUND_BUTTON);
        summaryPanel.setPreferredSize(new Dimension(0, 120));

        summaryText = new JLabel();
        summaryText.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        summaryText.setFont(NORMAL_FONT.deriveFont(13f));
        summaryText.setForeground(TEXT_PRIMARY_COLOR);
        updateSummaryLabel();
        summaryPanel.add(summaryText, BorderLayout.WEST);

        centerPanel.add(summaryPanel);

        root.add(centerPanel, BorderLayout.CENTER);

        /* ==== 이벤트 ==== */

        addSubButton.addActionListener(e -> showCartDialog());

        groupBtn.addActionListener(e -> Router.getInstance().navigateUser(Routes.GROUP_LIST));
        basketBtn.addActionListener(e -> Router.getInstance().navigateUser(Routes.STORE));

        // 처음 화면 갱신
        refreshSubscriptionList();

        return root;
    }

    /* ====================== 장바구니 팝업/로직 ====================== */

    private void showCartDialog() {
        // 예시 데이터
        String[] serviceNames = {
            "YouTube", "Netflix", "Spotify", "Disney+",
            "직접 입력(기타 서비스)"
        };
        int[] servicePrices = {
            19900, 17900, 10900, 9900,
            0
        };
        String[] serviceCategories = {
            "엔터테인먼트", "OTT", "음악", "OTT",
            ""
        };

        String choice = (String) JOptionPane.showInputDialog(
            this,
            "추가할 구독을 선택하세요.",
            "장바구니",
            JOptionPane.PLAIN_MESSAGE,
            null,
            serviceNames,
            serviceNames[0]
        );

        if (choice == null) return;

        int idx = Arrays.asList(serviceNames).indexOf(choice);
        if (idx == -1) return;

        if (choice.startsWith("직접 입력")) {
            showCustomAddDialog();
            return;
        }

        addSubscription(serviceCategories[idx], serviceNames[idx], servicePrices[idx]);
        JOptionPane.showMessageDialog(this, choice + " 구독을 추가했습니다.", "구독 추가", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showCustomAddDialog() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField categoryField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("서비스명"));
        panel.add(nameField);
        panel.add(new JLabel("월 금액(원)"));
        panel.add(priceField);
        panel.add(new JLabel("카테고리"));
        panel.add(categoryField);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "기타 서비스 추가",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return;

        try {
            int price = Integer.parseInt(priceField.getText().trim());
            addSubscription(categoryField.getText().trim(), nameField.getText().trim(), price);
            JOptionPane.showMessageDialog(this, nameField.getText() + " 구독을 추가했습니다.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "입력 오류: 금액은 숫자여야 합니다.", "오류", JOptionPane.WARNING_MESSAGE);
        }
    }

    // ✅ 그룹핑 신청 시 호출할 메서드 (1/4 가격)
    public void addGroupedSubscription(String serviceName) {
        if (serviceName == null || serviceName.isBlank()) return;

        String category;
        int fullPrice;

        switch (serviceName) {
            case "YouTube":
                category = "엔터테인먼트";
                fullPrice = 19900;
                break;
            case "Netflix":
                category = "OTT";
                fullPrice = 17900;
                break;
            case "Spotify":
                category = "음악";
                fullPrice = 10900;
                break;
            case "Disney+":
                category = "OTT";
                fullPrice = 9900;
                break;
            default:
                category = "기타(그룹핑)";
                fullPrice = 0;
        }

        int groupPrice = fullPrice / 4;
        addSubscription(category, serviceName + " (그룹핑)", groupPrice);
    }

    // ✅ 공통 추가 로직
    private void addSubscription(String category, String name, int price) {
        if (category == null || category.isBlank()) {
            category = "기타";
        }

        List<SubscriptionItem> list = categorySubs.computeIfAbsent(category, k -> new ArrayList<>());
        String display = name + " - " + price + "원/월";
        list.add(new SubscriptionItem(display, price));

        totalPrice += price;
        updateSummaryLabel();
        refreshSubscriptionList();
    }

    // ✅ 리스트 패널 다시 그리기 (카테고리 + 각 항목 + 취소 버튼)
    private void refreshSubscriptionList() {
        subsListPanel.removeAll();

        if (categorySubs.isEmpty()) {
            JLabel empty1 = new JLabel("아직 담은 구독이 없습니다.");
            empty1.setFont(SMALL_FONT);
            empty1.setForeground(TEXT_PRIMARY_COLOR);

            JLabel empty2 = new JLabel("장바구니에서 구독을 추가해보세요.");
            empty2.setFont(SMALL_FONT);
            empty2.setForeground(TEXT_PRIMARY_COLOR);

            subsListPanel.add(empty1);
            subsListPanel.add(Box.createVerticalStrut(4));
            subsListPanel.add(empty2);
        } else {
            List<String> sortedCategories = new ArrayList<>(categorySubs.keySet());
            Collections.sort(sortedCategories);

            for (String category : sortedCategories) {
                // 카테고리 제목
                JLabel catLabel = new JLabel("[" + category + "]");
                catLabel.setFont(NORMAL_FONT.deriveFont(Font.BOLD, 13f));
                catLabel.setForeground(TEXT_PRIMARY_COLOR);
                catLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 2, 0));
                subsListPanel.add(catLabel);

                List<SubscriptionItem> items = categorySubs.get(category);
                if (items == null) continue;

                for (SubscriptionItem item : new ArrayList<>(items)) {
                    JPanel row = new JPanel();
                    row.setOpaque(false);
                    row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                    row.setAlignmentX(Component.LEFT_ALIGNMENT);

                    int rowHeight = 26;
                    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));

                    JLabel subLabel = new JLabel("  " + item.display);
                    subLabel.setFont(SMALL_FONT);
                    subLabel.setForeground(TEXT_PRIMARY_COLOR);

                    JButton cancelBtn = new JButton("구독 취소");
                    styleFlatButton(cancelBtn);
                    cancelBtn.setFont(SMALL_FONT.deriveFont(11f));
                    cancelBtn.setPreferredSize(new Dimension(90, rowHeight));

                    final String catKey = category;
                    final SubscriptionItem currentItem = item;

                    cancelBtn.addActionListener(e -> {
                        List<SubscriptionItem> list = categorySubs.get(catKey);
                        if (list != null) {
                            list.remove(currentItem);
                            if (list.isEmpty()) {
                                categorySubs.remove(catKey);
                            }
                        }
                        totalPrice -= currentItem.price;
                        if (totalPrice < 0) totalPrice = 0;
                        updateSummaryLabel();
                        refreshSubscriptionList();
                    });

                    // X축으로: [텍스트]  -  (여백)  -  [버튼]
                    row.add(subLabel);
                    row.add(Box.createHorizontalGlue());
                    row.add(cancelBtn);

                    subsListPanel.add(row);
                }


                subsListPanel.add(Box.createVerticalStrut(8));
            }
        }

        subsListPanel.revalidate();
        subsListPanel.repaint();
    }

    private void updateSummaryLabel() {
        summaryText.setText("<html>지금까지 담은 & 구독한 서비스들<br/>= 총 월 "
                + won(totalPrice) + "</html>");
    }

    /* ====================== 공통 스타일 함수 ====================== */

    private static void styleFlatButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(NORMAL_FONT);
        btn.setBackground(BACKGROUND_BUTTON);
        btn.setForeground(TEXT_PRIMARY_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }
}
