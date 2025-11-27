package view.user.shared.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.SubscriptionService;
import util.UIConstants;

/**
 * 구독 서비스 정보(서비스명, 금액)를 표시하는 패널 컴포넌트
 * 데이터는 외부에서 주입받고, UI만 렌더링함
 */
public class InformationSection extends JPanel {

    public InformationSection(SubscriptionService subscription) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(java.awt.Color.WHITE);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 16, 20, 16));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 160));

        // 서비스명과 이미지를 담을 패널
        JPanel serviceNamePanel = new JPanel();
        serviceNamePanel.setLayout(new BoxLayout(serviceNamePanel, BoxLayout.X_AXIS));
        serviceNamePanel.setOpaque(false);
        serviceNamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        serviceNamePanel.setAlignmentX(0.0f);

        // 이미지 추가
        ImageIcon icon = ImageLoader.loadImage(subscription.getServiceName(), 32, 32);
        if (icon != null) {
            JLabel imageLabel = new JLabel(icon);
            serviceNamePanel.add(imageLabel);
            serviceNamePanel.add(Box.createHorizontalStrut(12));
        }

        // 서비스명 라벨
        JLabel serviceNameLabel = new JLabel(subscription.getServiceName());
        serviceNameLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 24));
        serviceNameLabel.setForeground(new Color(51, 51, 51));
        serviceNamePanel.add(serviceNameLabel);

        add(serviceNamePanel);
        add(Box.createVerticalStrut(12));

        if (subscription.getNumberOfUsers() > 1) {
            // 공유 중인 경우: 개인 부담금을 메인으로 표시
            int personalCost = subscription.getPersonalCost();
            JLabel personalCostLabel = new JLabel(String.format("%,d원", personalCost));
            personalCostLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 40));
            personalCostLabel.setForeground(new Color(33, 33, 33));
            personalCostLabel.setAlignmentX(0.0f);
            add(personalCostLabel);

            add(Box.createVerticalStrut(8));

            // 공유 인원 라벨
            JLabel sharingLabel = new JLabel(subscription.getNumberOfUsers() + "명이 공유 중 (총 " + String.format("%,d원", subscription.getAmount()) + ")");
            sharingLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 12));
            sharingLabel.setForeground(new Color(100, 100, 100));
            sharingLabel.setAlignmentX(0.0f);
            add(sharingLabel);
        } else {
            // 혼자 사용하는 경우: 월 요금을 메인으로 표시
            JLabel amountLabel = new JLabel(String.format("%,d원", subscription.getAmount()));
            amountLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 40));
            amountLabel.setForeground(new Color(33, 33, 33));
            amountLabel.setAlignmentX(0.0f);
            add(amountLabel);
        }
    }
}
