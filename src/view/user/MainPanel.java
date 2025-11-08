package view.user;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Router;
import util.Routes;
import util.UIConstants;
import view.layout.UserLayout;

public class MainPanel extends UserLayout {

    public MainPanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("HELLO MAIN");
        label.setFont(UIConstants.LARGE_FONT);
        panel.add(label);

        JButton toAlertButton = new JButton("Navigate to Alert");
        toAlertButton.setFont(UIConstants.NORMAL_FONT);
        toAlertButton.setFocusPainted(false);
        toAlertButton.setBorderPainted(false);
        toAlertButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        toAlertButton.addActionListener(e -> {
            Router.getInstance().navigateUser(Routes.ALERT);
        });
        toAlertButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                toAlertButton.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                toAlertButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });
        panel.add(toAlertButton);

        JButton toTransactionButton = new JButton("Navigation to Transaction");
        toTransactionButton.setFont(UIConstants.NORMAL_FONT);
        toTransactionButton.setFocusPainted(false);
        toTransactionButton.setBorderPainted(false);
        toTransactionButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        toTransactionButton.addActionListener(e -> {
            Router.getInstance().navigateUser(Routes.TRANSACTION);
        });
        toTransactionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                toTransactionButton.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                toTransactionButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });
        panel.add(toTransactionButton);

        return panel;
    }
}
