package view.admin;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;

import util.Routes;

public class AdminSidePanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentContainer;

    public AdminSidePanel() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);

        contentContainer.add(new AdminMainPanel(), Routes.ADMIN_MAIN);
        contentContainer.add(new AdminStatisticsPanel(), Routes.ADMIN_STATISTICS);
        contentContainer.add(new AdminSubscriptionManagePanel(), Routes.ADMIN_SUBSCRIPTION_MANAGE);
        contentContainer.add(new AdminTransactionPanel(), Routes.ADMIN_TRANSACTION_MANAGE);
        contentContainer.add(new AdminDashboardPanel(), Routes.ADMIN_DASHBOARD);

        add(contentContainer, BorderLayout.CENTER);

        // 🔥 처음 화면을 대시보드로 변경
        cardLayout.show(contentContainer, Routes.ADMIN_DASHBOARD);
    }

    public void switchTo(String screen) {
        cardLayout.show(contentContainer, screen);
    }
}
