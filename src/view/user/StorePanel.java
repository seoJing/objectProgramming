package view.user;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Router;
import util.Routes;
import util.UIConstants;
import view.layout.UserLayout;

public class StorePanel extends UserLayout {

    public StorePanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("HELLO STORE");
        label.setFont(UIConstants.LARGE_FONT);
        panel.add(label);

        JButton toGroupButton = new JButton("Navigate to Group");
        toGroupButton.setFont(UIConstants.NORMAL_FONT);
        toGroupButton.setFocusPainted(false);
        toGroupButton.setBorderPainted(false);
        toGroupButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        toGroupButton.addActionListener(e -> {
            Router.getInstance().navigateUser(Routes.GROUP);
        });
        toGroupButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                toGroupButton.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                toGroupButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });
        panel.add(toGroupButton);

        return panel;
    }
}
