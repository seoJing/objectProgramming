package view.user;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JPanel;

import util.Routes;

public class UserSidePanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel contentContainer;

    public UserSidePanel() {
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        contentContainer = new JPanel(cardLayout);

        contentContainer.add(new SubscriptionPanel(), Routes.SUBSCRIPTION);
        contentContainer.add(new SubscriptionDetailPanel(), Routes.SUBSCRIPTION_DETAIL);
        contentContainer.add(new TransactionPanel(), Routes.TRANSACTION);
        contentContainer.add(new TransactionDetailPanel(), Routes.TRANSACTION_DETAIL);
        contentContainer.add(new AccountPanel(), Routes.ACCOUNT);
        contentContainer.add(new MainPanel(), Routes.MAIN);
        contentContainer.add(new AlertPanel(), Routes.ALERT);
        contentContainer.add(new storePanel(), Routes.STORE);
        contentContainer.add(new storeDetailPanel(), Routes.STORE_DETAIL);
        contentContainer.add(new groupPanel(), Routes.GROUP);
        contentContainer.add(new settingPanel(), Routes.SETTING);

        add(contentContainer, BorderLayout.CENTER);

        cardLayout.show(contentContainer, Routes.MAIN);
    }

    public void switchTo(String screen) {
        cardLayout.show(contentContainer, screen);
    }
}
