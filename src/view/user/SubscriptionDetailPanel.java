package view.user;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.UIConstants;
import view.layout.UserLayout;

public class SubscriptionDetailPanel extends UserLayout {

    public SubscriptionDetailPanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel label = new JLabel("HELLO SUBSCRIPTION_DETAIL");
        label.setFont(UIConstants.LARGE_FONT);
        panel.add(label);

        return panel;
    }
}
