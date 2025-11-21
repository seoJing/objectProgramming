package view.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AdminSidePanel extends JPanel {

    public AdminSidePanel(ActionListener listener) {
        setLayout(new GridLayout(5, 1));
        setPreferredSize(new Dimension(180, 600));

        JButton btnMain = new JButton("메인");
        JButton btnUsers = new JButton("사용자 조회");
        JButton btnSubscription = new JButton("구독 관리");
        JButton btnStatistics = new JButton("통계 보기");
        JButton btnLogout = new JButton("로그아웃");

        // ⭐ 우리가 정의한 관리자 패널 이벤트 코드
        btnMain.setActionCommand("MAIN");
        btnUsers.setActionCommand("USER_VIEW");
        btnSubscription.setActionCommand("SUB_MANAGE");
        btnStatistics.setActionCommand("STATISTICS");
        btnLogout.setActionCommand("LOGOUT");

        btnMain.addActionListener(listener);
        btnUsers.addActionListener(listener);
        btnSubscription.addActionListener(listener);
        btnStatistics.addActionListener(listener);
        btnLogout.addActionListener(listener);

        add(btnMain);
        add(btnUsers);
        add(btnSubscription);
        add(btnStatistics);
        add(btnLogout);
    }
}
