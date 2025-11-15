package view.user;

import static util.UIConstants.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.*;

import view.user.shared.component.PanelHeader;
import view.user.shared.layout.UserLayout;
import view.user.NavigateUser;

/**
 * 내 구독 - 장바구니 / 카테고리별 구독 리스트 / 총액 화면
 */
public class SubscriptionAddPanel extends UserLayout {

    // 카테고리별 구독 저장용
    private final Map<String, java.util.List<String>> categorySubs = new HashMap<>();

    // 총 금액
    private int totalPrice = 0;
    private JLabel summaryText;
    private JTextArea subsArea;

    public SubscriptionAddPanel() {
        super();
        buildUI();
    }

    /* ====================== UI 구성 ====================== */

    private void buildUI() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_LIGHT);

        // 상단 헤더
        PanelHeader header = new PanelHeader("SUBMATE");
        header.setPreferredSize(HEADER_SIZE);
        add(header, BorderLayout.NORTH);

        // 가운데 메인 컨텐츠
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(centerPanel, BorderLayout.CENTER);

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

        JLabel listTitle = new JLabel("카테고리별 구독 리스트");
        listTitle.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        listTitle.setFont(NORMAL_FONT.deriveFont(Font.BOLD, 13f));
        listTitle.setForeground(TEXT_PRIMARY_COLOR);
        listOuter.add(listTitle, BorderLayout.NORTH);

        subsArea = new JTextArea("아직 담은 구독이 없습니다.\n장바구니에서 구독을 추가해보세요.");
        subsArea.setEditable(false);
        subsArea.setFont(SMALL_FONT);
        subsArea.setLineWrap(true);
        subsArea.setBackground(WHITE);
        subsArea.setForeground(TEXT_PRIMARY_COLOR);

        JScrollPane subsScroll = new JScrollPane(subsArea);
        subsScroll.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
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

        JPanel dotPanel = new JPanel();
        dotPanel.setOpaque(false);
        dotPanel.setLayout(new BoxLayout(dotPanel, BoxLayout.Y_AXIS));
        dotPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 16));
        dotPanel.add(createGreenDot());
        dotPanel.add(Box.createVerticalStrut(8));
        dotPanel.add(createGreenDot());
        dotPanel.add(Box.createVerticalStrut(8));
        dotPanel.add(createGreenDot());
        summaryPanel.add(dotPanel, BorderLayout.EAST);

        centerPanel.add(summaryPanel);

        // ===== 하단 네비게이션 =====
        JPanel bottomNav = new JPanel(new GridLayout(1, 4));
        bottomNav.setPreferredSize(NAV_SIZE);
        bottomNav.setBackground(POS_GREEN);
        bottomNav.add(createNavButton("홈", false));
        bottomNav.add(createNavButton("계좌", false));
        bottomNav.add(createNavButton("내 구독", true));
        bottomNav.add(createNavButton("구독 스토어", false));
        add(bottomNav, BorderLayout.SOUTH);

        /* ==== 이벤트 ==== */

        // 장바구니: 팝업 띄워서 구독 추가
        basketBtn.addActionListener(e -> showCartDialog());

        // 그룹핑: 두 번째 페이지로 전환
        groupBtn.addActionListener(e -> NavigateUser.goTo(new GroupListPanel()));
    }

    /* ====================== 장바구니 팝업/로직 ====================== */

    private void showCartDialog() {
        // 예시 데이터
        String[] serviceNames = {
            "예시1", "예시2", "예시3", "예시4",
            "직접 입력(기타 서비스)"
        };
        int[] servicePrices = {
            10000, 12000, 9000, 15000,
            0
        };
        String[] serviceCategories = {
            "카테고리A", "카테고리A", "카테고리B", "카테고리C",
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

    private void addSubscription(String category, String name, int price) {
        if (!categorySubs.containsKey(category)) {
            categorySubs.put(category, new ArrayList<>());
        }
        categorySubs.get(category).add(name + " - " + price + "원/월");
        totalPrice += price;
        updateSummaryLabel();
        refreshSubscriptionList();
    }

    private void refreshSubscriptionList() {
        if (categorySubs.isEmpty()) {
            subsArea.setText("아직 담은 구독이 없습니다.\n장바구니에서 구독을 추가해보세요.");
            return;
        }

        java.util.List<String> sortedCategories = new ArrayList<>(categorySubs.keySet());
        Collections.sort(sortedCategories);

        StringBuilder sb = new StringBuilder();
        for (String category : sortedCategories) {
            sb.append("[").append(category).append("]\n");
            for (String sub : categorySubs.get(category)) {
                sb.append("  ").append(sub).append("\n");
            }
            sb.append("\n");
        }
        subsArea.setText(sb.toString());
    }

    private void updateSummaryLabel() {
        summaryText.setText("<html>지금까지 담은 & 구독한 서비스들<br/>= 총 월 "
                + won(totalPrice) + "</html>");
    }

    /* ====================== 공통 스타일 함수 ====================== */

    private static JButton createNavButton(String text, boolean selected) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(NORMAL_FONT);
        if (selected) {
            btn.setBackground(WHITE);
            btn.setForeground(POS_GREEN);
        } else {
            btn.setBackground(POS_GREEN);
            btn.setForeground(WHITE);
        }
        return btn;
    }

    private static void styleFlatButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(NORMAL_FONT);
        btn.setBackground(BACKGROUND_BUTTON);
        btn.setForeground(TEXT_PRIMARY_COLOR);
        Border padding = BorderFactory.createEmptyBorder(8, 8, 8, 8);
        btn.setBorder(padding);
    }

    private JComponent createGreenDot() {
        JLabel dot = new JLabel("●");
        dot.setForeground(POS_GREEN);
        dot.setFont(dot.getFont().deriveFont(20f));
        dot.setAlignmentX(Component.CENTER_ALIGNMENT);
        return dot;
    }
}
