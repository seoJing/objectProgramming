package view.layout;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public abstract class BaseLayout extends JPanel {
    protected JPanel contentPanel;

    public BaseLayout() {
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        add(createNavigation(), BorderLayout.SOUTH);
    }

    protected abstract JPanel createHeader();
    protected abstract JPanel createNavigation();

    protected void setContent(JPanel content) {
        contentPanel.removeAll();
        contentPanel.add(content, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
