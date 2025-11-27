package view.user.shared.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.UIConstants;

public class AlertItemPanel extends JPanel {

    public AlertItemPanel(String title) {
        setLayout(new BorderLayout());
        setMaximumSize(UIConstants.ITEM_WIDTH_MAX_100);
        setPreferredSize(new Dimension(0, UIConstants.PANEL_HEIGHT_100));
        setBackground(UIConstants.ITEM_BG_LIGHT);
        setBorder(null);
        setOpaque(false);

        setUI(new javax.swing.plaf.basic.BasicPanelUI() {
            @Override
            public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                int shadowSize = 6;
                for (int i = shadowSize; i > 0; i--) {
                    g2d.setColor(new Color(0, 0, 0, (int) (12 * (1 - i / (float) shadowSize))));
                    g2d.fillRoundRect(i, i, c.getWidth() - 2 * i, c.getHeight() - 2 * i, 12, 12);
                }

                g2d.setColor(c.getBackground());
                g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);

                super.paint(g, c);
            }
        });

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // 좌측 - 제목 (텍스트 자동 개행)
        JLabel titleLabel = new JLabel("<html><body style='width:250px'>" + title + "</body></html>");
        titleLabel.setFont(UIConstants.SMALL_FONT_13);
        titleLabel.setForeground(UIConstants.TEXT_PRIMARY);
        add(titleLabel, BorderLayout.CENTER);

        JButton deleteBtn = new JButton("삭제") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed()) {
                    g2d.setColor(UIConstants.LIGHT_BG_HOVER);
                } else {
                    g2d.setColor(UIConstants.VERY_LIGHT_BG);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        deleteBtn.setFont(UIConstants.NORMAL_FONT_BOLD);
        deleteBtn.setPreferredSize(UIConstants.ICON_32x32);
        deleteBtn.setMaximumSize(UIConstants.ICON_32x32);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setOpaque(false);
        deleteBtn.setForeground(UIConstants.DARK_GRAY_170);
        deleteBtn.addActionListener(e -> {
            java.awt.Container parent = AlertItemPanel.this.getParent();
            if (parent != null) {
                java.awt.Component toRemove = AlertItemPanel.this;
                java.awt.Container removeParent = parent;

                if (parent instanceof javax.swing.JPanel &&
                    ((javax.swing.JPanel) parent).getComponentCount() == 1) {
                    java.awt.Container grandParent = parent.getParent();
                    if (grandParent != null) {
                        toRemove = (java.awt.Component) parent;
                        removeParent = grandParent;
                    }
                }

                int index = -1;
                for (int i = 0; i < removeParent.getComponentCount(); i++) {
                    if (removeParent.getComponent(i) == toRemove) {
                        index = i;
                        break;
                    }
                }

                removeParent.remove(toRemove);

                if (index >= 0 && index < removeParent.getComponentCount()) {
                    java.awt.Component next = removeParent.getComponent(index);
                    if (next instanceof javax.swing.Box.Filler) {
                        removeParent.remove(next);
                    }
                }
                removeParent.revalidate();
                removeParent.repaint();
            }
        });

        add(deleteBtn, BorderLayout.EAST);
    }
}
