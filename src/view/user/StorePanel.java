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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.util.ArrayList;
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

import model.SubscriptionList;
import model.SubscriptionService;
import model.User;
import model.UserList;
import util.DataLoader;
import util.Router;
import util.Routes;
import util.SessionManager;
import view.layout.UserLayout;

// StorePanel - 구독 장바구니 관리 화면 / (void → void)
/**
 * 내 구독 - 장바구니 / 카테고리별 구독 리스트 / 총액 화면
 */
public class StorePanel extends UserLayout {

    private static StorePanel instance;

    private static class SubscriptionItem {
        String display;
        int price;
        int numberOfUsers;

        SubscriptionItem(String display, int price, int numberOfUsers) {
            this.display = display;
            this.price = price;
            this.numberOfUsers = numberOfUsers;
        }
    }

    private final Map<String, List<SubscriptionItem>> categorySubs = new HashMap<>();
    private int totalPrice = 0;
    private JLabel summaryText;
    private JPanel subsListPanel;

    public StorePanel() {
        super();
        instance = this;
        setContent(createContent());
    }

    public static StorePanel getInstance() {
        return instance;
    }

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND_LIGHT);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel basketLabel = new JLabel("구독 장바구니", SwingConstants.CENTER);
        basketLabel.setFont(SMALL_FONT.deriveFont(Font.PLAIN, 13f));
        basketLabel.setForeground(TEXT_PRIMARY_COLOR);
        basketLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(basketLabel);
        centerPanel.add(Box.createVerticalStrut(8));

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

        JPanel listOuter = new JPanel(new BorderLayout());
        listOuter.setBackground(BACKGROUND_BUTTON);
        listOuter.setPreferredSize(new Dimension(0, 260));

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

        subsListPanel = new JPanel();
        subsListPanel.setOpaque(false);
        subsListPanel.setLayout(new BoxLayout(subsListPanel, BoxLayout.Y_AXIS));

        JScrollPane subsScroll = new JScrollPane(subsListPanel);
        subsScroll.setBorder(BorderFactory.createEmptyBorder(0, 8, 8, 8));
        subsScroll.getVerticalScrollBar().setUnitIncrement(16);
        listOuter.add(subsScroll, BorderLayout.CENTER);

        centerPanel.add(listOuter);
        centerPanel.add(Box.createVerticalStrut(16));

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
        centerPanel.add(Box.createVerticalStrut(8));

        // ===== 저장/취소 버튼 =====
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, BUTTON_HORIZONTAL_GAP, 0));
        buttonPanel.setOpaque(false);

        JButton saveBtn = new JButton("저장");
        JButton cancelBtn = new JButton("취소");
        styleFlatButton(saveBtn);
        styleFlatButton(cancelBtn);

        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        centerPanel.add(buttonPanel);

        root.add(centerPanel, BorderLayout.CENTER);

        /* ==== 이벤트 ==== */

        addSubButton.addActionListener(e -> showCartDialog());
        saveBtn.addActionListener(e -> saveSubs());
        cancelBtn.addActionListener(e -> clearCart());

        groupBtn.addActionListener(e -> Router.getInstance().navigateUser(Routes.GROUP_LIST));
        basketBtn.addActionListener(e -> Router.getInstance().navigateUser(Routes.STORE));

        refreshSubscriptionList();

        return root;
    }

    /* ====================== 장바구니 팝업/로직 ====================== */

    private void showCartDialog() {
        List<SubscriptionService> sortedSubscriptions = SubscriptionList.getInstance().getSortedByName();

        // 현재 로그인 유저의 Ledger 가져오기
        User currentUser = SessionManager.getInstance().getCurrentUser();
        List<SubscriptionService> availableSubscriptions = new ArrayList<>();

        // 이미 구독 중인 서비스는 제외
        if (currentUser != null) {
            for (SubscriptionService sub : sortedSubscriptions) {
                if (!currentUser.getLedger().isSubscribed(sub.getServiceName())) {
                    availableSubscriptions.add(sub);
                }
            }
        } else {
            availableSubscriptions = sortedSubscriptions;
        }

        if (availableSubscriptions.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "추가할 수 있는 상품 정보가 없습니다.",
                    "알림",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String[] options = new String[availableSubscriptions.size() + 1];
        int idx = 0;
        for (SubscriptionService sub : availableSubscriptions) {
            options[idx++] = sub.getServiceName() + " - " + sub.getAmount() + "원";
        }
        options[availableSubscriptions.size()] = "직접 입력(기타 서비스)";

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "추가할 구독을 선택하세요.",
                "장바구니",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == null) return;

        if (choice.startsWith("직접 입력")) {
            showCustomAddDialog();
            return;
        }

        SubscriptionService selectedProduct = null;
        for (SubscriptionService sub : availableSubscriptions) {
            String label = sub.getServiceName() + " - " + sub.getAmount() + "원";
            if (label.equals(choice)) {
                selectedProduct = sub;
                break;
            }
        }
        if (selectedProduct == null) return;

        Map<String, DataLoader.ProductInfo> productMap = DataLoader.getProductMap();
        DataLoader.ProductInfo productInfo = findProductInfoByName(selectedProduct.getServiceName(), productMap);
        String category = (productInfo != null) ? productInfo.category : "기타";

        addSubscription(category, selectedProduct.getServiceName(), selectedProduct.getAmount());
        JOptionPane.showMessageDialog(
                this,
                selectedProduct.getServiceName() + " 구독을 추가했습니다.",
                "구독 추가",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private DataLoader.ProductInfo findProductInfoByName(String name, Map<String, DataLoader.ProductInfo> productMap) {
        for (DataLoader.ProductInfo p : productMap.values()) {
            if (p.name.equals(name)) {
                return p;
            }
        }
        return null;
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

    public void addGroupedSubscription(String serviceName, int people) {
        if (serviceName == null || serviceName.isBlank()) return;
        if (people <= 0) people = 1;

        // 현재 로그인 유저 확인
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getLedger().isSubscribed(serviceName)) {
            JOptionPane.showMessageDialog(null, "이미 구독 중인 서비스입니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DataLoader.ProductInfo base = findProductForService(serviceName);

        if (base == null) {
            JOptionPane.showMessageDialog(null, "상품 정보를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        addSubscription(base.category, base.name, base.price, people);
    }

    private DataLoader.ProductInfo findProductForService(String serviceName) {
        String keyword;
        switch (serviceName) {
            case "YouTube":  keyword = "유튜브"; break;
            case "Netflix":  keyword = "넷플릭스"; break;
            case "Spotify":  keyword = "스포티파이"; break;
            case "Disney+":  keyword = "디즈니플러스"; break;
            default:         keyword = null;
        }
        if (keyword == null) return null;

        for (DataLoader.ProductInfo p : DataLoader.getProductMap().values()) {
            if (p.name.contains(keyword)) {
                return p;
            }
        }
        return null;
    }

    private void addSubscription(String category, String name, int price) {
        addSubscription(category, name, price, 1);
    }

    private void addSubscription(String category, String name, int price, int numberOfUsers) {
        if (category == null || category.isBlank()) {
            category = "기타";
        }

        List<SubscriptionItem> list = categorySubs.computeIfAbsent(category, k -> new ArrayList<>());
        String display = name + " - " + price + "원/월";

        list.add(new SubscriptionItem(display, price, numberOfUsers));

        totalPrice += price;
        updateSummaryLabel();
        refreshSubscriptionList();
    }


    /* ====================== 리스트 패널 그리기 ====================== */
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

                    JButton cancelBtn = new JButton("취소");
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

    private static void styleFlatButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(NORMAL_FONT);
        btn.setBackground(BACKGROUND_BUTTON);
        btn.setForeground(TEXT_PRIMARY_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }

    private void saveSubs() {
        if (categorySubs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "저장할 구독이 없습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        SessionManager sm = SessionManager.getInstance();
        User sessionUser = (sm != null) ? sm.getCurrentUser() : null;

        if (sessionUser == null) {
            JOptionPane.showMessageDialog(this, "로그인된 사용자가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserList userList = UserList.getInstance();
        User targetUser = userList.findById(sessionUser.getId());

        if (targetUser == null) {
            JOptionPane.showMessageDialog(this, "사용자 정보를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int addedCount = 0;
        for (List<SubscriptionItem> items : categorySubs.values()) {
            for (SubscriptionItem item : items) {
                SubscriptionService service = new SubscriptionService(
                        item.display.split(" - ")[0],
                        item.price,
                        LocalDate.now().toString(),
                        sessionUser.getId(),
                        12,
                        item.numberOfUsers
                );

                targetUser.getLedger().addSubscription(service);
                addedCount++;
            }
        }

        System.out.println("[장바구니 저장] " + addedCount + "개의 구독이 추가되었습니다.");
        JOptionPane.showMessageDialog(this, addedCount + "개의 구독이 저장되었습니다.", "완료", JOptionPane.INFORMATION_MESSAGE);

        clearCart();
        Router.getInstance().navigateUser(Routes.MAIN);
    }

    private void clearCart() {
        categorySubs.clear();
        totalPrice = 0;
        updateSummaryLabel();
        refreshSubscriptionList();
    }
}
