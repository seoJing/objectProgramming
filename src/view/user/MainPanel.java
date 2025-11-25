package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
        SessionManager.getInstance().setSelectedDate(picked);          // 날짜 저장(옵션)
        Router.getInstance().navigateUser(Routes.ALL_TRANSACTIONS);    // 전체 거래 화면으로 이동
    });

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
        calCard.setPreferredSize(new Dimension(520, 360));     // 고정 너비/높이
        calCard.setMaximumSize(new Dimension(520, 360));
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
        // AlertItemPanel을 래퍼 패널로 감싸서 클릭 시 alertPanel로 이동
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        wrapperPanel.setBackground(Color.WHITE);

        // 동적으로 생성된 알람 데이터 - 다음 결제일이 임박한 구독부터 우선 표시
        String alertMessage = generateAlertMessage();

        // 알람이 있으면 표시, 없으면 빈 패널 반환
        if (alertMessage == null || alertMessage.isEmpty()) {
            return wrapperPanel;
        }

        AlertItemPanel alertItem = new AlertItemPanel(alertMessage);
        alertItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // 알람 클릭 시 alertPanel로 이동
        alertItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // 삭제 버튼이 아닌 영역 클릭 시
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

        // 다음 결제일 기준으로 정렬 (가장 임박한 순서)
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

        // 오늘부터의 남은 일수 계산
        LocalDate nextPaymentDate = LocalDate.parse(nextSubscription.getNextPaymentDate(), formatter);
        LocalDate today = LocalDate.now();
        long daysUntilPayment = java.time.temporal.ChronoUnit.DAYS.between(today, nextPaymentDate);

        // 일수에 따른 메시지 생성
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
        containerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        containerPanel.setBackground(Color.WHITE);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
        listPanel.setBackground(Color.WHITE);

        // 현재 사용자의 실제 구독 서비스 데이터 조회
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

                // 부모 클래스의 paintComponent 호출 (아이콘, 텍스트 등 렌더링)
                super.paintComponent(g);
            }
        };

        // 이미지 아이콘 설정 (서비스별로 다른 이미지 사용)
        // ImageLoader가 .png 확장자를 자동으로 추가하므로 서비스명만 전달
        ImageIcon icon = ImageLoader.loadImage(subscription.getServiceName(), 60, 60);

        if (icon != null) {
            btn.setIcon(icon);
        } else {
            // 이미지가 없으면 텍스트로 표시
            btn.setText("<html><center>" + subscription.getServiceName() + "</center></html>");
        }

        btn.setPreferredSize(new Dimension(80, 80));
        btn.setMaximumSize(new Dimension(80, 80));
        btn.setMinimumSize(new Dimension(80, 80));
        btn.setBackground(UIConstants.BACKGROUND_BUTTON);
        btn.setForeground(UIConstants.USER_HEADER_COLOR);
        btn.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.addActionListener(e -> {
            SessionManager.getInstance().setSelectedSubscription(subscription);
            Router.getInstance().navigateUser(Routes.SUBSCRIPTION_DETAIL);
        });

        return btn;
    }
}
