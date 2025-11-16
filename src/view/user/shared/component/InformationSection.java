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
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        setPreferredSize(new Dimension(Integer.MAX_VALUE, 150));

        // 서비스명과 이미지를 담을 패널
        JPanel serviceNamePanel = new JPanel();
        serviceNamePanel.setLayout(new BoxLayout(serviceNamePanel, BoxLayout.X_AXIS));
        serviceNamePanel.setOpaque(false);
        serviceNamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        serviceNamePanel.setAlignmentX(0.0f); // 왼쪽 정렬

        // 이미지 추가
        String imageName = subscription.getServiceName().toLowerCase() + ".png";
        ImageIcon icon = ImageLoader.loadImage(imageName, 32, 32);
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

        // 금액 라벨
        JLabel amountLabel = new JLabel(String.format("%,d원", subscription.getAmount()));
        amountLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 40));
        amountLabel.setForeground(new Color(33, 33, 33));
        amountLabel.setAlignmentX(0.0f); // 왼쪽 정렬
        add(amountLabel);
    }
}
