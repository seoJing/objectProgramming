package view.admin;

import model.User;
import model.UserList;
import model.SubscriptionService;
import model.Account;

import javax.swing.*;
import java.awt.*;

public class AdminMainPanel extends JPanel {

    public AdminMainPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ====== 제목 ======
        JLabel title = new JLabel("관리자 대시보드", SwingConstants.CENTER);
        title.setFont(new Font("Pretendard", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ====== 중앙 통계 패널 ======
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        int totalUsers = UserList.getInstance().size();
        int totalAccounts = countAllAccounts();
        int totalSubs = countAllSubscriptions();
        int totalExpectedSales = calculateExpectedSales();

        statsPanel.add(createCard("총 사용자 수", totalUsers + "명"));
        statsPanel.add(createCard("총 계좌 수", totalAccounts + "개"));
        statsPanel.add(createCard("총 구독 서비스", totalSubs + "개"));
        statsPanel.add(createCard("월 예상 매출", totalExpectedSales + "원"));

        add(statsPanel, BorderLayout.CENTER);
    }

    // ===== 카드 UI 생성 메서드 =====
    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(new Color(245, 245, 245));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Pretendard", Font.PLAIN, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Pretendard", Font.BOLD, 22));

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    // ===== 통계 계산 메서드 =====

    private int countAllAccounts() {
        int sum = 0;
        for (User u : UserList.getInstance().getAll()) {
            sum += u.getAccountList().size();
        }
        return sum;
    }

    private int countAllSubscriptions() {
        int sum = 0;
        for (User u : UserList.getInstance().getAll()) {
            sum += u.getLedger().getSubscriptionList().size();
        }
        return sum;
    }

    private int calculateExpectedSales() {
        int sum = 0;
        for (User u : UserList.getInstance().getAll()) {
            for (SubscriptionService s : u.getLedger().getSubscriptionList()) {
                sum += s.getAmount();
            }
        }
        return sum;
    }
}
