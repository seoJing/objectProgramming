package view.user;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.UIConstants;
import view.layout.UserLayout;

public class MyPagePanel extends UserLayout {

    public MyPagePanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel label = new JLabel("HELLO MY_PAGE");
        label.setFont(UIConstants.LARGE_FONT);
        panel.add(label);

        return panel;
    }
}
