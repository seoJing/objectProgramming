package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
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
            // 화면이 보일 때마다 새로 콘텐츠 생성 (구독 해지 후 반영)
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

        // 현재 사용자의 실제 구독 서비스 데이터 조회
        User user = SessionManager.getInstance().getCurrentUser();
        List<SubscriptionService> subscriptions = new ArrayList<>();
        if (user != null && user.getLedger() != null) {
            subscriptions = user.getLedger().getSubscriptionList();
        }

        if (subscriptions.isEmpty()) {
            JLabel emptyLabel = new JLabel("구독 중인 서비스가 없습니다.");
            emptyLabel.setFont(UIConstants.NORMAL_FONT);
            emptyLabel.setForeground(new Color(150, 150, 150));
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

        itemPanel.setBackground(new Color(240, 245, 250));
        itemPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        itemPanel.setOpaque(false);
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        itemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                SessionManager.getInstance().setSelectedSubscription(subscription);
                Router.getInstance().navigateUser(Routes.SUBSCRIPTION_DETAIL);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                itemPanel.setBackground(new Color(230, 240, 245));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                itemPanel.setBackground(new Color(240, 245, 250));
            }
        });

        // 좌측: 서비스명 및 결제 정보
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel serviceNameLabel = new JLabel(subscription.getServiceName());
        serviceNameLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        serviceNameLabel.setForeground(new Color(33, 33, 33));
        leftPanel.add(serviceNameLabel);

        String paymentInfo = String.format("결제일: %s | 금액: %,d원", subscription.getPaymentDate(), subscription.getAmount());

        JLabel paymentInfoLabel = new JLabel(paymentInfo);
        paymentInfoLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 11));
        paymentInfoLabel.setForeground(new Color(120, 120, 120));
        leftPanel.add(paymentInfoLabel);

        itemPanel.add(leftPanel, BorderLayout.WEST);

        // 우측: 금액 (공유 계정이면 실제 구독료, 아니면 전체 금액)
        int displayAmount = SubscriptionSavingsUtil.calculateActualPrice(subscription);
        JLabel amountLabel = new JLabel(String.format("%,d원", displayAmount));
        amountLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        amountLabel.setForeground(new Color(11, 218, 81));
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

        expensePanel.setBackground(new Color(245, 248, 250));
        expensePanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        expensePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
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
        totalLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 18));
        totalLabel.setForeground(new Color(11, 218, 81));

        expensePanel.add(totalLabel, BorderLayout.EAST);

        return expensePanel;
    }

    private JPanel createSuggestionSection() {
        JPanel suggestionPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 40));
        suggestionPanel.setBackground(Color.WHITE);
        suggestionPanel.setBorder(BorderFactory.createEmptyBorder(20, 16, 40, 16));
        suggestionPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 120));

        JButton suggestionBtn = new JButton("이런 구독은 어때요?") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                // 배경을 투명하게 처리하므로 paint 호출 안 함
                super.paintComponent(g);
            }
        };

        suggestionBtn.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 14));
        suggestionBtn.setForeground(new Color(11, 218, 81));
        suggestionBtn.setBackground(new Color(255, 255, 255, 0)); // 투명
        suggestionBtn.setFocusPainted(false);
        suggestionBtn.setBorderPainted(false);
        suggestionBtn.setContentAreaFilled(false);
        suggestionBtn.setOpaque(false);
        suggestionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 언더라인 효과를 위한 HTML 사용
        suggestionBtn.setText("<html><u>이런 구독은 어때요?</u></html>");

        suggestionBtn.addActionListener(e -> {
            Router.getInstance().navigateUser(Routes.STORE);
        });

        suggestionPanel.add(suggestionBtn);

        return suggestionPanel;
    }
}
