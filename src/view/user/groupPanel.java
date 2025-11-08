package view.user;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Router;
import util.Routes;
import util.UIConstants;
import view.layout.UserLayout;

public class groupPanel extends UserLayout {

    public groupPanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("HELLO GROUP");
        label.setFont(UIConstants.LARGE_FONT);
        panel.add(label);

         JButton toStoreDetailButton = new JButton("Navigate to StoreDetail");
        toStoreDetailButton.setFont(UIConstants.NORMAL_FONT);
        toStoreDetailButton.setFocusPainted(false);
        toStoreDetailButton.setBorderPainted(false);
        toStoreDetailButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        toStoreDetailButton.addActionListener(e -> {
            Router.getInstance().navigateUser(Routes.STORE_DETAIL);
        });
        toStoreDetailButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                toStoreDetailButton.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                toStoreDetailButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });
        panel.add(toStoreDetailButton);

        return panel;
    }
}
