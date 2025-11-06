package view.login;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.UIConstants;

public class LoginPanel extends JPanel {
    public LoginPanel() {
        setLayout(new GridBagLayout());

        JLabel label = new JLabel("HELLO LOGIN");
        label.setFont(UIConstants.LARGE_FONT);
        add(label);
    }
}
