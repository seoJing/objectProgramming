package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.SubscriptionService;
import util.Router;
import util.Routes;
import util.SessionManager;
import util.UIConstants;
import view.layout.UserLayout;
import view.user.shared.component.AlertItemPanel;
import view.user.shared.component.ImageLoader;

public class MainPanel extends UserLayout {

    /**
     * Mock 구독 서비스 데이터 (5개)
     */
    private SubscriptionService[] getMockSubscriptions() {
        return new SubscriptionService[] {
            new SubscriptionService("YouTube", 19900, "2024-12-01", "tjwlsrb1021", 12, 1),
            new SubscriptionService("Netflix", 17900, "2024-12-05", "tjwlsrb1021", 12, 2),
            new SubscriptionService("Spotify", 10900, "2024-12-10", "tjwlsrb1021", 12, 3),
            new SubscriptionService("Disney+", 9900, "2024-12-15", "tjwlsrb1021", 12, 1),
            new SubscriptionService("Apple Music", 10900, "2024-12-20", "tjwlsrb1021", 12, 2)
        };
    }

    /**
     * Mock 알람 데이터 (5개)
     */
    private String[] getMockAlerts() {
        return new String[] {
            "2일 후에 유튜브 프리미엄 결제 예정이에요. 연결된 계좌 잔액이 부족해요.",
            "넷플릭스 구독이 3일 뒤에 갱신됩니다. 결제일을 확인해주세요.",
            "Spotify 프리미엄 결제가 1주일 남았습니다. 미리 확인해주세요.",
            "Disney+ 구독료 결제가 내일입니다. 준비 바랍니다.",
            "Apple Music 결제가 예정되어 있습니다. 계좌 잔액을 확인하세요."
        };
    }

    public MainPanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        // 1. 끌 수 있는 알림창
        panel.add(createAlertNotification());

        // 2. 구독 서비스 아이콘 가로 스크롤 리스트
        panel.add(createSubscriptionScrollList());

        // 3. 캘린더 (단색 박스)
        panel.add(createCalendarBox());
        panel.add(Box.createVerticalStrut(10));

        // 4. 지출 리스트 (단색 박스)
        panel.add(createExpenseListBox());

        return panel;
    }

    private JPanel createAlertNotification() {
        // AlertItemPanel을 래퍼 패널로 감싸서 클릭 시 alertPanel로 이동
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setOpaque(false);
        wrapperPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        wrapperPanel.setBackground(Color.WHITE);

        // Mock 알람 데이터 - 첫 번째 알람만 표시
        String[] mockAlerts = getMockAlerts();
        AlertItemPanel alertItem = new AlertItemPanel(mockAlerts[0]);
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

    private JPanel createSubscriptionScrollList() {
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        containerPanel.setBackground(Color.WHITE);
        containerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
        listPanel.setBackground(Color.WHITE);

        // Mock 구독 서비스 데이터
        SubscriptionService[] mockSubscriptions = getMockSubscriptions();
        for (int i = 0; i < mockSubscriptions.length; i++) {
            JButton serviceBtn = createSubscriptionButton(mockSubscriptions[i], i);
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
        String imageName = subscription.getServiceName().toLowerCase() + ".png";
        ImageIcon icon = ImageLoader.loadImage(imageName, 60, 60);

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

    private JPanel createCalendarBox() {
        JPanel boxPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2d.setColor(UIConstants.SHADOW_LIGHT);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        boxPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        boxPanel.setBackground(UIConstants.BACKGROUND_LIGHT);
        boxPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        boxPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("이번달 지출 캘린더");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY_COLOR);
        boxPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel contentLabel = new JLabel("캘린더 (추후 구현 예정)");
        contentLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 12));
        contentLabel.setForeground(UIConstants.TEXT_DISABLED_COLOR);
        boxPanel.add(contentLabel, BorderLayout.CENTER);

        return boxPanel;
    }

    private JPanel createExpenseListBox() {
        JPanel boxPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2d.setColor(UIConstants.SHADOW_LIGHT);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
            }
        };
        boxPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 600));
        boxPanel.setBackground(UIConstants.BACKGROUND_LIGHT);
        boxPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        boxPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("지출 목록");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY_COLOR);
        boxPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel contentLabel = new JLabel("날짜별 지출 목록 (추후 구현 예정)");
        contentLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 12));
        contentLabel.setForeground(UIConstants.TEXT_DISABLED_COLOR);
        boxPanel.add(contentLabel, BorderLayout.CENTER);

        return boxPanel;
    }
}
