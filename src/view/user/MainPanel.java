package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.SubscriptionService;
import model.User;
import util.Router;
import util.Routes;
import util.SessionManager;
import util.UIConstants;
import view.layout.UserLayout;
import view.user.shared.component.AlertItemPanel;
import view.user.shared.component.Calendar;
import view.user.shared.component.ImageLoader;

public class MainPanel extends UserLayout {

    public MainPanel() {
        super();
    }

    Calendar cal = new Calendar(LocalDate.now(), picked -> {
        SessionManager.getInstance().setSelectedDate(picked);
        SessionManager.getInstance().setPreviousRoute(Routes.MAIN);
        Router.getInstance().navigateUser(Routes.ALL_TRANSACTIONS);
    });

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            JPanel content = createContent();
            setContent(content);
        }
    }

    private JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // 1. 끌 수 있는 알림창
        panel.add(createAlertNotification());

        // 2. 캘린더
        JPanel calCard = new JPanel(new BorderLayout());
        calCard.setOpaque(true);
        calCard.setBackground(UIConstants.CARD_BG);
        calCard.setBorder(UIConstants.CARD_BORDER);
        calCard.setPreferredSize(UIConstants.CALENDAR_SIZE);
        calCard.setMaximumSize(UIConstants.CALENDAR_SIZE);
        calCard.add(cal, BorderLayout.CENTER);
        panel.add(calCard);
        panel.add(Box.createVerticalStrut(12));

        // 3. 구독 서비스 아이콘 가로 스크롤 리스트
        panel.add(createSubscriptionScrollList());
        panel.add(Box.createVerticalStrut(8));
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createAlertNotification() {
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setMaximumSize(UIConstants.ITEM_WIDTH_MAX_100);
        wrapperPanel.setBackground(Color.WHITE);

        String alertMessage = generateAlertMessage();

        if (alertMessage == null || alertMessage.isEmpty()) {
            return wrapperPanel;
        }

        AlertItemPanel alertItem = new AlertItemPanel(alertMessage);
        alertItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        alertItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getX() < alertItem.getWidth() - 50) {
                    Router.getInstance().navigateUser(Routes.ALERT);
                }
            }
        });

        wrapperPanel.add(alertItem, BorderLayout.CENTER);
        return wrapperPanel;
    }

    /**
     * 현재 사용자의 구독 정보를 기반으로 동적 알람 메시지 생성
     * 다음 결제일이 가장 임박한 구독을 우선으로 표시
     */
    private String generateAlertMessage() {
        User user = SessionManager.getInstance().getCurrentUser();

        if (user == null || user.getLedger() == null) {
            return "";
        }

        List<SubscriptionService> subscriptions = user.getLedger().getSubscriptionList();
        if (subscriptions == null || subscriptions.isEmpty()) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SubscriptionService nextSubscription = subscriptions.stream()
            .min((s1, s2) -> {
                LocalDate date1 = LocalDate.parse(s1.getNextPaymentDate(), formatter);
                LocalDate date2 = LocalDate.parse(s2.getNextPaymentDate(), formatter);
                return date1.compareTo(date2);
            })
            .orElse(null);

        if (nextSubscription == null) {
            return "";
        }

        LocalDate nextPaymentDate = LocalDate.parse(nextSubscription.getNextPaymentDate(), formatter);
        LocalDate today = LocalDate.now();
        long daysUntilPayment = java.time.temporal.ChronoUnit.DAYS.between(today, nextPaymentDate);

        if (daysUntilPayment <= 1) {
            return "내일 " + nextSubscription.getServiceName() + " 결제가 예정됩니다. 계좌 잔액을 확인하세요.";
        } else if (daysUntilPayment <= 7) {
            return daysUntilPayment + "일 후 " + nextSubscription.getServiceName() + " 결제 예정입니다. 준비 바랍니다.";
        } else if (daysUntilPayment <= 30) {
            return nextSubscription.getServiceName() + " 구독이 " + daysUntilPayment + "일 뒤에 갱신됩니다. 결제일을 확인해주세요.";
        } else {
            return nextSubscription.getServiceName() + " 결제가 예정되어 있습니다. 계좌 잔액을 확인하세요.";
        }
    }

    private JPanel createSubscriptionScrollList() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setMaximumSize(UIConstants.ITEM_WIDTH_MAX_120);
        containerPanel.setBackground(Color.WHITE);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
        listPanel.setBackground(Color.WHITE);

        User user = SessionManager.getInstance().getCurrentUser();
        List<SubscriptionService> subscriptions = new ArrayList<>();

        if (user != null && user.getLedger() != null) {
            List<SubscriptionService> userSubscriptions = user.getLedger().getSubscriptionList();
            if (userSubscriptions != null) {
                subscriptions = userSubscriptions;
            }
        }

        // 구독 서비스 버튼 생성
        for (int i = 0; i < subscriptions.size(); i++) {
            JButton serviceBtn = createSubscriptionButton(subscriptions.get(i), i);
            listPanel.add(serviceBtn);
            listPanel.add(Box.createHorizontalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);

        // 마우스 휠로 가로 스크롤
        scrollPane.addMouseWheelListener(e -> {
            int scrollAmount = e.getUnitsToScroll() * UIConstants.SCROLL_AMOUNT;
            scrollPane.getHorizontalScrollBar().setValue(
                scrollPane.getHorizontalScrollBar().getValue() + scrollAmount
            );
        });

        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private JButton createSubscriptionButton(SubscriptionService subscription, int index) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                super.paintComponent(g);
            }
        };

        ImageIcon icon = ImageLoader.loadImage(subscription.getServiceName(), 60, 60);

        if (icon != null) {
            btn.setIcon(icon);
        } else {
            btn.setText("<html><center>" + subscription.getServiceName() + "</center></html>");
        }

        btn.setPreferredSize(UIConstants.BUTTON_80x80);
        btn.setMaximumSize(UIConstants.BUTTON_80x80);
        btn.setMinimumSize(UIConstants.BUTTON_80x80);
        btn.setBackground(UIConstants.BACKGROUND_BUTTON);
        btn.setForeground(UIConstants.USER_HEADER_COLOR);
        btn.setFont(UIConstants.SMALL_FONT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.addActionListener(e -> {
            SessionManager.getInstance().setSelectedSubscription(subscription);
            SessionManager.getInstance().setPreviousRoute(Routes.MAIN);
            Router.getInstance().navigateUser(Routes.SUBSCRIPTION_DETAIL);
        });

        return btn;
    }
}
