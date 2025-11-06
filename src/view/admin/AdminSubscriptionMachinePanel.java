package view.admin;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.UIConstants;
import view.layout.AdminLayout;

public class AdminSubscriptionMachinePanel extends AdminLayout {

    public AdminSubscriptionMachinePanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel label = new JLabel("HELLO ADMIN_SUBSCRIPTION_MACHINE");
        label.setFont(UIConstants.LARGE_FONT);
        panel.add(label);

        return panel;
    }
}
