package view.admin;

import javax.swing.*;
import java.awt.*;
import util.Routes;
import util.Router;
import util.UIConstants;
import view.layout.AdminLayout;

public class AdminDashboardPanel extends AdminLayout {

    public AdminDashboardPanel() {
        super();
        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1) 제목
        JLabel title = new JLabel("관리자 대시보드");
        title.setFont(UIConstants.LARGE_FONT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // 2) 카드들
        panel.add(createSummaryCard("전체 구독 수", "128건"));
        panel.add(Box.createVerticalStrut(15));

        panel.add(createSummaryCard("이번 달 결제 금액", "1,530,000원"));
        panel.add(Box.createVerticalStrut(15));

        panel.add(createSummaryCard("만료 예정 구독", "23건"));
        panel.add(Box.createVerticalStrut(25));

        // 3) 빠른 이동 버튼
        panel.add(createQuickActionPanel());
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createSummaryCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(UIConstants.CARD_BG);
        card.setBorder(UIConstants.CARD_BORDER);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        card.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 16));
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 0));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 24));
        valueLabel.setForeground(UIConstants.ACCENT_COLOR);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 0));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createQuickActionPanel() {

        JPanel box = new JPanel();
        box.setLayout(new GridLayout(1, 3, 15, 0));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        box.setBackground(Color.WHITE);

        JButton subscriptionBtn = createButton("구독 관리");
        subscriptionBtn.addActionListener(e ->
                Router.getInstance().navigateAdmin(Routes.ADMIN_SUBSCRIPTION_MANAGE)
        );

        JButton transactionBtn = createButton("결제 내역");
        transactionBtn.addActionListener(e ->
                Router.getInstance().navigateAdmin(Routes.ADMIN_TRANSACTION_MANAGE)
        );

        JButton statisticsBtn = createButton("통계 보기");
        statisticsBtn.addActionListener(e ->
                Router.getInstance().navigateAdmin(Routes.ADMIN_STATISTICS)
        );

        box.add(subscriptionBtn);
        box.add(transactionBtn);
        box.add(statisticsBtn);

        return box;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(UIConstants.BACKGROUND_BUTTON);
        
        // 🔥 글자색을 PRIMARY COLOR로 변경
        btn.setForeground(UIConstants.TEXT_PRIMARY_COLOR);

        btn.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
        return btn;
    }
}
