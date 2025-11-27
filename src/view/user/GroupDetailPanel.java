package view.user;

import static util.UIConstants.*;

import javax.swing.*;
import java.awt.*;

import view.layout.UserLayout;

public class GroupDetailPanel extends UserLayout {

    // 선택된 서비스 이름 저장용 static 변수 (예: "YouTube", "Netflix"...)
    private static String selectedServiceName = "";

    public static void setSelectedServiceName(String name) {
        selectedServiceName = name;
    }

    public GroupDetailPanel() {
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

        JLabel title = new JLabel("구독 장바구니", SwingConstants.CENTER);
        title.setFont(SMALL_FONT.deriveFont(13f));
        title.setForeground(TEXT_PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(title);
        center.add(Box.createVerticalStrut(16));

        // 안내문
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(BACKGROUND_BUTTON);
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoLabel = new JLabel(
                "<html><body>"
                        + "<b>안내사항</b><br/>"
                        + "- 그룹핑은 동일 서비스의 요금을 여러 명이 분담해 사용하는 기능입니다.<br/>"
                        + "- 그룹장은 결제 및 관리 권한을 가지고, 구성원은 초대 시 참여할 수 있습니다.<br/>"
                        + "- 인원 변경 시 다음 결제일 기준으로 요금이 자동 재계산됩니다.<br/>"
                        + "- 서비스 이용이 시작되기까지 시간이 소요될 수 있습니다.<br/><br/>"

                        + "<b>그룹 정보</b><br/>"
                        + "- 인원이 모집되면 해당 인원 기준으로 요금이 확정됩니다.<br/>"
                        + "- 요금 = 서비스 정가 ÷ 참여 인원 방식으로 계산됩니다.<br/>"
                        + "- 그룹장은 멤버 초대, 추방, 결제수단 변경 등의 권한을 가집니다.<br/><br/>"

                        + "<b>환불 규정</b><br/>"
                        + "- 환불/탈퇴는 그룹장 승인 후 다음 결제일부터 적용됩니다.<br/>"
                        + "- 사용 중 기간에 대한 부분 환불은 제공되지 않습니다.<br/>"
                        + "- 그룹장이 중도 해지해도 동일한 규정이 적용됩니다.<br/>"
                        + "</body></html>"
        );
        infoLabel.setFont(NORMAL_FONT.deriveFont(13f));
        infoLabel.setForeground(TEXT_PRIMARY_COLOR);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        infoPanel.add(infoLabel, BorderLayout.CENTER);
        center.add(infoPanel);
        center.add(Box.createVerticalStrut(24));

        // 신청 버튼
        JButton applyBtn = new JButton("그룹핑 신청");
        applyBtn.setBackground(BACKGROUND_BUTTON);
        applyBtn.setForeground(TEXT_PRIMARY_COLOR);
        applyBtn.setFocusPainted(false);
        applyBtn.setFont(NORMAL_FONT);
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        applyBtn.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        center.add(applyBtn);
        center.add(Box.createVerticalStrut(24));

        applyBtn.addActionListener(e -> {
    // 1) 서비스 선택 확인
    if (selectedServiceName == null || selectedServiceName.isBlank()) {
        JOptionPane.showMessageDialog(
                this,
                "선택된 서비스가 없습니다.",
                "알림",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // 2) 몇 명이서 나눌지 입력 받기
    String input = JOptionPane.showInputDialog(
            this,
            "몇 명이서 나눠서 사용하나요?",
            "그룹 인원 입력",
            JOptionPane.PLAIN_MESSAGE
    );

    if (input == null) {
        // 취소 누른 경우
        return;
    }

    int people;
    try {
        people = Integer.parseInt(input.trim());
        if (people <= 0) {
            throw new NumberFormatException();
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
                this,
                "1 이상의 숫자를 입력해주세요.",
                "입력 오류",
                JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // 3) StorePanel에 그룹핑 구독 추가 (1/n 반영)
    StorePanel store = StorePanel.getInstance();
    if (store != null) {
        store.addGroupedSubscription(selectedServiceName, people);
    }

    // 4) 완료 메시지
    String msg = selectedServiceName + " " + people + "인 그룹핑 신청이 완료되었습니다.";
    JOptionPane.showMessageDialog(
            this,
            msg,
            "알림",
            JOptionPane.INFORMATION_MESSAGE
    );

    // 5) 장바구니(STORE)로 라우팅
    util.Router.getInstance().navigateUser(util.Routes.STORE);
});


        root.add(center, BorderLayout.CENTER);
        return root;
    }
}
