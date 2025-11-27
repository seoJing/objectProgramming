package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Account;
import model.SubscriptionService;
import model.Transaction;
import model.User;
import util.Router;
import util.Routes;
import util.SessionManager;
import util.SubscriptionSavingsUtil;
import util.UIConstants;
import view.layout.UserLayout;
import view.user.shared.component.InformationSection;
import view.user.shared.component.PanelHeader;
import view.user.shared.component.RoundedButton;
import view.user.shared.component.TransactionHistoryList;

public class SubscriptionDetailPanel extends UserLayout {

    /**
     * 선택된 구독의 실제 거래 내역을 조회합니다.
     * 모든 계좌에서 해당 구독의 거래를 필터링하여 반환합니다.
     *
     * @param subscription 조회할 구독 서비스
     * @return 해당 구독의 거래 내역 리스트
     */
    private List<Transaction> getSubscriptionTransactions(SubscriptionService subscription) {
        List<Transaction> transactions = new ArrayList<>();

        User user = SessionManager.getInstance().getCurrentUser();
        if (user == null) {
            return transactions;
        }

        String baseName = subscription.getServiceName().split("[_\\-]")[0];

        for (Account account : user.getAccountList()) {
            for (Transaction transaction : account.getTransactionList()) {
                if (transaction.getLocation().equals(subscription.getServiceName()) ||
                    transaction.getLocation().equals(baseName)) {
                    transactions.add(transaction);
                }
            }
        }

        transactions.sort((t1, t2) -> t2.getDateTime().compareTo(t1.getDateTime()));

        return transactions;
    }

    public SubscriptionDetailPanel() {
        super();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            JPanel content = createContent();
            setContent(content);
        }
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        SubscriptionService subscription = SessionManager.getInstance().getSelectedSubscription();
        List<Transaction> transactions = getSubscriptionTransactions(subscription);

        if (subscription != null) {
            // 상단 헤더
            panel.add(new PanelHeader("구독 상세보기"), BorderLayout.NORTH);

            // 중앙 섹션 (정보 + 거래내역 + 절약액)
            JPanel centerSection = new JPanel(new BorderLayout(0, 16));
            centerSection.setBackground(Color.WHITE);
            centerSection.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

            // 상단 정보
            centerSection.add(new InformationSection(subscription), BorderLayout.NORTH);

            // 거래 내역
            centerSection.add(new TransactionHistoryList(transactions), BorderLayout.CENTER);

            // 절약액 UI (거래내역 아래)
            JPanel savingsPanel = createSavingsPanel(subscription);
            if (savingsPanel != null) {
                centerSection.add(savingsPanel, BorderLayout.SOUTH);
            }

            panel.add(centerSection, BorderLayout.CENTER);

            // 하단 버튼 섹션
            panel.add(createButtonSection(subscription), BorderLayout.SOUTH);
        }

        return panel;
    }

    private JPanel createButtonSection(SubscriptionService subscription) {
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 40));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));
        buttonPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 180));

        User user = SessionManager.getInstance().getCurrentUser();

        RoundedButton editBtn = new RoundedButton("구독 해지", UIConstants.BUTTON_COLOR, e -> {
            System.out.println("[구독 해지 시작] " + subscription.getServiceName());
            System.out.println("해지 전 구독 개수: " + user.getLedger().getSubscriptionList().size());
            System.out.println("대상 구독: " + subscription);

            boolean removed = user.getLedger().getSubscriptionList().remove(subscription);
            System.out.println("제거 결과: " + removed);
            System.out.println("해지 후 구독 개수: " + user.getLedger().getSubscriptionList().size());

            Router.getInstance().navigateUser(Routes.SUBSCRIPTION);
        });
        RoundedButton cancelBtn = new RoundedButton("연결 계좌 변경", UIConstants.BUTTON_COLOR, e -> {
            System.out.println(((RoundedButton) e.getSource()).getText());
        });

        buttonPanel.add(editBtn);
        buttonPanel.add(cancelBtn);

        return buttonPanel;
    }

    /**
     * 절약액 UI 패널 생성
     */
    private JPanel createSavingsPanel(SubscriptionService subscription) {
        Object[] savings = SubscriptionSavingsUtil.calculateSavings(subscription);
        if (savings == null) {
            return null;
        }

        String message = (String) savings[1];

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // underline 처리된 텍스트 라벨
        JLabel savingsLabel = new JLabel(message);
        savingsLabel.setFont(new Font(UIConstants.FONT_FAMILY, Font.PLAIN, 12));
        savingsLabel.setForeground(new Color(51, 51, 51));
        savingsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        savingsLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(51, 51, 51)));

        savingsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                Router.getInstance().navigateUser(Routes.GROUP_LIST);
            }
        });

        panel.add(savingsLabel, BorderLayout.WEST);

        return panel;
    }

}
