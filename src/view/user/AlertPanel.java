package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.SubscriptionService;
import model.User;
import util.Router;
import util.Routes;
import util.SessionManager;
import view.layout.UserLayout;
import view.user.shared.component.AlertItemPanel;
import view.user.shared.component.PanelHeader;

public class AlertPanel extends UserLayout {

    /**
     * 모든 구독의 알람 메시지를 동적으로 생성합니다.
     * 각 구독의 다음 결제일을 기반으로 알람을 만들고 결제일 순으로 정렬합니다.
     *
     * @return 생성된 알람 메시지 리스트
     */
    private List<String> generateAlertMessages() {
        List<String> alertMessages = new ArrayList<>();

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null || user.getLedger() == null) {
            return alertMessages;
        }

        List<SubscriptionService> subscriptions = user.getLedger().getSubscriptionList();
        if (subscriptions == null || subscriptions.isEmpty()) {
            return alertMessages;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        subscriptions.sort((s1, s2) -> {
            LocalDate date1 = LocalDate.parse(s1.getNextPaymentDate(), formatter);
            LocalDate date2 = LocalDate.parse(s2.getNextPaymentDate(), formatter);
            return date1.compareTo(date2);
        });

        for (SubscriptionService sub : subscriptions) {
            String alertMessage = generateAlertMessage(sub);
            if (alertMessage != null && !alertMessage.isEmpty()) {
                alertMessages.add(alertMessage);
            }
        }

        return alertMessages;
    }

    /**
     * 단일 구독의 알람 메시지를 생성합니다.
     */
    private String generateAlertMessage(SubscriptionService subscription) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate nextPaymentDate = LocalDate.parse(subscription.getNextPaymentDate(), formatter);
        LocalDate today = LocalDate.now();
        long daysUntilPayment = java.time.temporal.ChronoUnit.DAYS.between(today, nextPaymentDate);

        if (daysUntilPayment <= 1) {
            return "내일 " + subscription.getServiceName() + " 결제가 예정됩니다. 계좌 잔액을 확인하세요.";
        } else if (daysUntilPayment <= 7) {
            return daysUntilPayment + "일 후 " + subscription.getServiceName() + " 결제 예정입니다. 준비 바랍니다.";
        } else if (daysUntilPayment <= 30) {
            return subscription.getServiceName() + " 구독이 " + daysUntilPayment + "일 뒤에 갱신됩니다. 결제일을 확인해주세요.";
        } else {
            return subscription.getServiceName() + " 결제가 예정되어 있습니다. 계좌 잔액을 확인하세요.";
        }
    }

    public AlertPanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // 상단 헤더
        panel.add(new PanelHeader("알람"), BorderLayout.NORTH);

        // 중앙 - 알람 리스트
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        List<String> alertMessages = generateAlertMessages();
        for (String alertMessage : alertMessages) {
            AlertItemPanel alertItemPanel = new AlertItemPanel(alertMessage);

            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false);
            wrapperPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            wrapperPanel.setBackground(Color.WHITE);

            alertItemPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            alertItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getX() < alertItemPanel.getWidth() - 50) {
                        Router.getInstance().navigateUser(Routes.SUBSCRIPTION);
                    }
                }
            });

            wrapperPanel.add(alertItemPanel, BorderLayout.CENTER);
            listPanel.add(wrapperPanel);
            listPanel.add(Box.createVerticalStrut(5));
        }

        listPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
