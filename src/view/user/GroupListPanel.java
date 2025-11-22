package view.user;

import static util.UIConstants.BACKGROUND_BUTTON;
import static util.UIConstants.BACKGROUND_LIGHT;
import static util.UIConstants.BUTTON_HORIZONTAL_GAP;
import static util.UIConstants.NORMAL_FONT;
import static util.UIConstants.SMALL_FONT;
import static util.UIConstants.TEXT_PRIMARY_COLOR;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import util.Router;
import util.Routes;
import view.layout.UserLayout;

/**
 * 두 번째 페이지 : 그룹핑 리스트
 */
public class GroupListPanel extends UserLayout {

    public GroupListPanel() {
        super();
        setContent(createContent());
    }

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND_LIGHT);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel label = new JLabel("구독 장바구니", SwingConstants.CENTER);
        label.setFont(SMALL_FONT.deriveFont(13f));
        label.setForeground(TEXT_PRIMARY_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(label);
        center.add(Box.createVerticalStrut(8));

        // 위 버튼 줄: 첫 페이지와 동일
        JPanel modePanel = new JPanel(new GridLayout(1, 2, BUTTON_HORIZONTAL_GAP, 0));
        modePanel.setOpaque(false);
        JButton basketBtn = new JButton("장바구니");
        JButton groupBtn  = new JButton("그룹핑");
        styleFlatButton(basketBtn);
        styleFlatButton(groupBtn);
        modePanel.add(basketBtn);
        modePanel.add(groupBtn);
        center.add(modePanel);
        center.add(Box.createVerticalStrut(16));

        // 아래 큰 카드: 그룹핑 묶음 가격 예시
        JPanel groupPanel = new JPanel(new BorderLayout());
        groupPanel.setBackground(BACKGROUND_BUTTON);

        int mergedHeight = 260 + 16 + 120; // 두 카드 합친 높이 근사
        groupPanel.setPreferredSize(new Dimension(0, mergedHeight));
        groupPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, mergedHeight));

        // 제목
        JLabel groupTitle = new JLabel("<그룹핑 묶음 가격>");
        groupTitle.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        groupTitle.setFont(NORMAL_FONT.deriveFont(13f));
        groupTitle.setForeground(TEXT_PRIMARY_COLOR);
        groupPanel.add(groupTitle, BorderLayout.NORTH);

        // 안쪽 내용 패널 (4개 서비스 + 신청하기 버튼)
        JPanel priceListPanel = new JPanel();
        priceListPanel.setOpaque(false);
        priceListPanel.setLayout(new BoxLayout(priceListPanel, BoxLayout.Y_AXIS));
        priceListPanel.setBorder(BorderFactory.createEmptyBorder(0, 16, 16, 16));

        // 각 행 생성: 라벨 + 신청하기 버튼
priceListPanel.add(createPriceRow(
        "YouTube 4인 그룹핑: 4,975원/월 (정가 19,900원)", "YouTube"
));
priceListPanel.add(Box.createVerticalStrut(6));

priceListPanel.add(createPriceRow(
        "Netflix 4인 그룹핑: 4,475원/월 (정가 17,900원)", "Netflix"
));
priceListPanel.add(Box.createVerticalStrut(6));

priceListPanel.add(createPriceRow(
        "Spotify 4인 그룹핑: 2,725원/월 (정가 10,900원)", "Spotify"
));
priceListPanel.add(Box.createVerticalStrut(6));

priceListPanel.add(createPriceRow(
        "Disney+ 4인 그룹핑: 2,475원/월 (정가 9,900원)", "Disney+"
));

        // 중앙에 리스트 붙이기
        groupPanel.add(priceListPanel, BorderLayout.CENTER);

        // 레이아웃 유지
        center.add(groupPanel);
        center.add(Box.createVerticalStrut(16));   // 카드와 아래 여백
        center.add(Box.createVerticalGlue());

        /* ==== 이벤트 ==== */

        // 장바구니 탭
        basketBtn.addActionListener(e -> Router.getInstance().navigateUser(Routes.STORE));

        // groupBtn은 현재 페이지(그룹핑 탭)이므로 선택만 유지하거나 필요 없으면 비워둠
        // groupBtn.addActionListener(e -> Router.getInstance().navigateUser(Routes.GROUP_LIST));

        root.add(center, BorderLayout.CENTER);
        return root;
    }

 
    private JPanel createPriceRow(String text, String serviceName) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);

        JTextArea textArea = new JTextArea(text);
        textArea.setFont(NORMAL_FONT.deriveFont(13f));
        textArea.setForeground(TEXT_PRIMARY_COLOR);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setLineWrap(true);          // 줄바꿈 켜기
        textArea.setWrapStyleWord(true);     // 단어 단위로 줄바꿈
        textArea.setFocusable(false);
        textArea.setBorder(null);

        JButton applyBtn = new JButton("신청하기");
        styleFlatButton(applyBtn);
        applyBtn.setPreferredSize(new Dimension(90, 30));

        applyBtn.addActionListener(e -> {
            GroupDetailPanel.setSelectedServiceName(serviceName);
            Router.getInstance().navigateUser(Routes.GROUP_DETAIL);
        });

        row.add(textArea, BorderLayout.CENTER);
        row.add(applyBtn, BorderLayout.EAST);

        return row;
    }



    private static void styleFlatButton(JButton btn) {
        btn.setFocusPainted(false);
        btn.setFont(NORMAL_FONT);
        btn.setBackground(BACKGROUND_BUTTON);
        btn.setForeground(TEXT_PRIMARY_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    }
}
