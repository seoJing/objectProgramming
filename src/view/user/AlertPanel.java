package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import util.Router;
import util.Routes;
import view.layout.UserLayout;
import view.user.shared.component.AlertItemPanel;
import view.user.shared.component.PanelHeader;

public class AlertPanel extends UserLayout {

    private String[] getMockAlerts() {
        return new String[] {
            "2일 후에 유튜브 프리미엄 결제 예정이에요. 연결된 계좌 잔액이 부족해요.",
            "넷플릭스 구독이 3일 뒤에 갱신됩니다. 결제일을 확인해주세요.",
            "Spotify 프리미엄 결제가 1주일 남았습니다. 미리 확인해주세요.",
            "Disney+ 구독료 결제가 내일입니다. 준비 바랍니다.",
            "Apple Music 결제가 예정되어 있습니다. 계좌 잔액을 확인하세요."
        };
    }

    public AlertPanel() {
        super();

        JPanel content = createContent();
        setContent(content);
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // 상단 헤더
        panel.add(new PanelHeader("알람"), BorderLayout.NORTH);

        // 중앙 - 알람 리스트
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Mock 알람 데이터
        String[] mockAlerts = getMockAlerts();

        // 샘플 알람 아이템들
        for (int i = 0; i < mockAlerts.length; i++) {
            AlertItemPanel alertItemPanel = new AlertItemPanel(mockAlerts[i]);

            // 알람 아이템을 래퍼 패널로 감싸서 클릭 시 페이지 이동
            JPanel wrapperPanel = new JPanel(new BorderLayout());
            wrapperPanel.setOpaque(false);
            wrapperPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            wrapperPanel.setBackground(Color.WHITE);

            alertItemPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            // 알람 클릭 시 특정 페이지로 이동
            alertItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    // 삭제 버튼이 아닌 영역 클릭 시
                    if (e.getX() < alertItemPanel.getWidth() - 50) {
                        Router.getInstance().navigateUser(Routes.SUBSCRIPTION);
                    }
                }
            });

            wrapperPanel.add(alertItemPanel, BorderLayout.CENTER);
            listPanel.add(wrapperPanel);
            listPanel.add(Box.createVerticalStrut(5));
        }

        listPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);

        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
