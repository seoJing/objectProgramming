package view.user;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Router;
import util.Routes;
import util.UIConstants;
import view.layout.UserLayout;

public class TransactionPanel extends UserLayout {

    public TransactionPanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("HELLO TRANSACTION");
        label.setFont(UIConstants.LARGE_FONT);
        panel.add(label);

        JButton toTransactionDetailButton = new JButton("Navigation to TransactionDetail");
        toTransactionDetailButton.setFont(UIConstants.NORMAL_FONT);
        toTransactionDetailButton.setFocusPainted(false);
        toTransactionDetailButton.setBorderPainted(false);
        toTransactionDetailButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        toTransactionDetailButton.addActionListener(e -> {
            Router.getInstance().navigateUser(Routes.TRANSACTION_DETAIL);
        });
        toTransactionDetailButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                toTransactionDetailButton.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                toTransactionDetailButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });
        panel.add(toTransactionDetailButton);

        return panel;
    }
}
