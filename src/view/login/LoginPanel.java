package view.login;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Router;
import util.Routes;
import util.UIConstants;

public class LoginPanel extends JPanel {
    public LoginPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("HELLO LOGIN");
        label.setFont(UIConstants.LARGE_FONT);
        add(label);

        JButton toAdminButton = new JButton("Navigate to Admin");
        toAdminButton.setFont(UIConstants.NORMAL_FONT);
        toAdminButton.setFocusPainted(false);
        toAdminButton.setBorderPainted(false);
        toAdminButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        toAdminButton.addActionListener(e -> {
            Router.getInstance().navigateTo(Routes.ADMIN);
        });
        toAdminButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                toAdminButton.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                toAdminButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });
        add(toAdminButton);

        JButton toUserButton = new JButton("Navigate to User");
        toUserButton.setFont(UIConstants.NORMAL_FONT);
        toUserButton.setFocusPainted(false);
        toUserButton.setBorderPainted(false);
        toUserButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        toUserButton.addActionListener(e -> {
            Router.getInstance().navigateTo(Routes.USER);
        });
        toUserButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                toUserButton.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                toUserButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });
        add(toUserButton);
    }
}
