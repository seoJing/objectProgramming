package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.SubscriptionService;
import model.User;
import util.Router;
import util.Routes;
import util.SessionManager;
import util.SubscriptionSavingsUtil;
import util.UIConstants;
import view.layout.UserLayout;
import view.user.shared.component.PanelHeader;

public class SubscriptionPanel extends UserLayout {

    public SubscriptionPanel() {
        super();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            JPanel content = createContent();
            setContent(content);
        }
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // 상단: 헤더
        panel.add(new PanelHeader("구독"), BorderLayout.NORTH);

        // 중앙: 구독 서비스 리스트
        panel.add(createSubscriptionListSection(), BorderLayout.CENTER);

        // 하단: "이런 구독은 어때요?" 버튼
        panel.add(createSuggestionSection(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSubscriptionListSection() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(Color.WHITE);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // 구독 서비스 리스트
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        User user = SessionManager.getInstance().getCurrentUser();
        List<SubscriptionService> subscriptions = new ArrayList<>();
        if (user != null && user.getLedger() != null) {
            subscriptions = user.getLedger().getSubscriptionList();
        }

        if (subscriptions.isEmpty()) {
            JLabel emptyLabel = new JLabel("구독 중인 서비스가 없습니다.");
            emptyLabel.setFont(UIConstants.NORMAL_FONT);
            emptyLabel.setForeground(UIConstants.MUTED_GRAY_150);
            listPanel.add(emptyLabel);
        } else {
            for (SubscriptionService subscription : subscriptions) {
                JPanel itemPanel = createSubscriptionItemPanel(subscription);
                listPanel.add(itemPanel);
                listPanel.add(Box.createVerticalStrut(8));
            }

            // 총 지출 정보
            listPanel.add(Box.createVerticalStrut(8));
            listPanel.add(createTotalExpensePanel(subscriptions));
        }

        // 스크롤 판
        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createSubscriptionItemPanel(SubscriptionService subscription) {
        JPanel itemPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };

        itemPanel.setBackground(UIConstants.LIGHT_BG);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        itemPanel.setMaximumSize(UIConstants.ITEM_WIDTH_MAX);
        itemPanel.setOpaque(false);
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        itemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                SessionManager.getInstance().setSelectedSubscription(subscription);
                SessionManager.getInstance().setPreviousRoute(Routes.SUBSCRIPTION);
                Router.getInstance().navigateUser(Routes.SUBSCRIPTION_DETAIL);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                itemPanel.setBackground(UIConstants.LIGHT_BG_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                itemPanel.setBackground(UIConstants.LIGHT_BG);
            }
        });

        // 좌측: 서비스명 및 결제 정보
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel serviceNameLabel = new JLabel(subscription.getServiceName());
        serviceNameLabel.setFont(UIConstants.NORMAL_FONT_BOLD);
        serviceNameLabel.setForeground(UIConstants.TEXT_PRIMARY);
        leftPanel.add(serviceNameLabel);

        String paymentInfo = String.format("결제일: %s | 금액: %,d원", subscription.getPaymentDate(), subscription.getAmount());

        JLabel paymentInfoLabel = new JLabel(paymentInfo);
        paymentInfoLabel.setFont(UIConstants.SMALL_FONT_11);
        paymentInfoLabel.setForeground(UIConstants.TEXT_SECONDARY);
        leftPanel.add(paymentInfoLabel);

        itemPanel.add(leftPanel, BorderLayout.WEST);

        // 우측: 금액 (공유 계정이면 실제 구독료, 아니면 전체 금액)
        int displayAmount = SubscriptionSavingsUtil.calculateActualPrice(subscription);
        JLabel amountLabel = new JLabel(String.format("%,d원", displayAmount));
        amountLabel.setFont(UIConstants.MEDIUM_FONT_16);
        amountLabel.setForeground(UIConstants.POS_GREEN);
        itemPanel.add(amountLabel, BorderLayout.EAST);

        return itemPanel;
    }

    private JPanel createTotalExpensePanel(List<SubscriptionService> subscriptions) {
        JPanel expensePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };

        expensePanel.setBackground(UIConstants.VERY_LIGHT_BG);
        expensePanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        expensePanel.setMaximumSize(UIConstants.HEADER_SIZE_MAX);
        expensePanel.setOpaque(false);

        // 좌측: 라벨
        JLabel labelText = new JLabel("총 구독료:");
        labelText.setFont(UIConstants.NORMAL_FONT);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(labelText);

        expensePanel.add(leftPanel, BorderLayout.WEST);

        // 우측: 총 금액 (공유 계정이면 실제 지불액으로 계산)
        int totalAmount = subscriptions.stream()
            .mapToInt(SubscriptionSavingsUtil::calculateActualPrice)
            .sum();
        JLabel totalLabel = new JLabel(String.format("%,d원", totalAmount));
        totalLabel.setFont(UIConstants.LARGE_FONT_18);
        totalLabel.setForeground(UIConstants.POS_GREEN);

        expensePanel.add(totalLabel, BorderLayout.EAST);

        return expensePanel;
    }

    private JPanel createSuggestionSection() {
        JPanel suggestionPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 40));
        suggestionPanel.setBackground(Color.WHITE);
        suggestionPanel.setBorder(BorderFactory.createEmptyBorder(20, 16, 40, 16));
        suggestionPanel.setPreferredSize(UIConstants.ITEM_WIDTH_MAX_120);

        JButton suggestionBtn = new JButton("이런 구독은 어때요?") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
            }
        };

        suggestionBtn.setFont(UIConstants.NORMAL_FONT);
        suggestionBtn.setForeground(UIConstants.POS_GREEN);
        suggestionBtn.setBackground(UIConstants.TRANSPARENT);
        suggestionBtn.setFocusPainted(false);
        suggestionBtn.setBorderPainted(false);
        suggestionBtn.setContentAreaFilled(false);
        suggestionBtn.setOpaque(false);
        suggestionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        suggestionBtn.setText("<html><u>이런 구독은 어때요?</u></html>");

        suggestionBtn.addActionListener(e -> {
            Router.getInstance().navigateUser(Routes.STORE);
        });

        suggestionPanel.add(suggestionBtn);

        return suggestionPanel;
    }
}
