package view.user.shared.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import util.UIConstants;

/**
 * 알람 아이템 컴포넌트
 * AlertPanel과 MainPanel에서 공유
 */
public class AlertItemPanel extends JPanel {

    public AlertItemPanel(String title) {
        setLayout(new BorderLayout());
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        setPreferredSize(new Dimension(0, 100));
        setBackground(new Color(248, 250, 252));
        setBorder(null);
        setOpaque(false);

        // 그림자 효과와 배경
        setUI(new javax.swing.plaf.basic.BasicPanelUI() {
            @Override
            public void paint(java.awt.Graphics g, javax.swing.JComponent c) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

                // 그림자 효과
                int shadowSize = 6;
                for (int i = shadowSize; i > 0; i--) {
                    g2d.setColor(new Color(0, 0, 0, (int) (12 * (1 - i / (float) shadowSize))));
                    g2d.fillRoundRect(i, i, c.getWidth() - 2 * i, c.getHeight() - 2 * i, 12, 12);
                }

                // 배경
                g2d.setColor(c.getBackground());
                g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);

                super.paint(g, c);
            }
        });

        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // 좌측 - 제목 (텍스트 자동 개행)
        JLabel titleLabel = new JLabel("<html><body style='width:250px'>" + title + "</body></html>");
        titleLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 13));
        titleLabel.setForeground(new Color(33, 33, 33));
        add(titleLabel, BorderLayout.CENTER);

        JButton deleteBtn = new JButton("삭제") {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
                g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed()) {
                    g2d.setColor(new Color(240, 240, 240));
                } else {
                    g2d.setColor(new Color(250, 250, 250));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 14));
        deleteBtn.setPreferredSize(new Dimension(32, 32));
        deleteBtn.setMaximumSize(new Dimension(32, 32));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.setContentAreaFilled(false);
        deleteBtn.setOpaque(false);
        deleteBtn.setForeground(new Color(170, 170, 170));
        deleteBtn.addActionListener(e -> {
            // 부모 패널에서 이 아이템 제거
            java.awt.Container parent = AlertItemPanel.this.getParent();
            if (parent != null) {
                // 제거할 컴포넌트 결정 (wrapper가 있으면 wrapper 제거, 아니면 자신 제거)
                java.awt.Component toRemove = AlertItemPanel.this;
                java.awt.Container removeParent = parent;

                // Wrapper로 감싸져 있는지 확인 (wrapperPanel은 BorderLayout 사용)
                if (parent instanceof javax.swing.JPanel &&
                    ((javax.swing.JPanel) parent).getComponentCount() == 1) {
                    // 부모가 AlertItemPanel만 포함하고 있으면 wrapper일 가능성
                    java.awt.Container grandParent = parent.getParent();
                    if (grandParent != null) {
                        toRemove = (java.awt.Component) parent;
                        removeParent = grandParent;
                    }
                }

                // 제거 전에 인덱스 찾기
                int index = -1;
                for (int i = 0; i < removeParent.getComponentCount(); i++) {
                    if (removeParent.getComponent(i) == toRemove) {
                        index = i;
                        break;
                    }
                }

                // 컴포넌트 제거
                removeParent.remove(toRemove);

                // 다음 컴포넌트가 Strut이면 제거
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
