package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.SubscriptionService;
import model.Transaction;
import util.SessionManager;
import util.UIConstants;
import view.layout.UserLayout;
import view.user.shared.component.InformationSection;
import view.user.shared.component.PanelHeader;
import view.user.shared.component.TransactionHistoryList;
import view.user.shared.component.RoundedButton;

public class SubscriptionDetailPanel extends UserLayout {

    private List<Transaction> generateMockTransactions(SubscriptionService subscription) {
        List<Transaction> transactions = new ArrayList<>();
        LocalDate today = LocalDate.now();

        // 최근 12개월의 거래 MOCK 내역 생성
//        for (int i = 0; i < 12; i++) {
//            LocalDate transactionDate = today.minusMonths(i);
//            transactions.add(new Transaction(
//                "구독료 결제",
//                subscription.getAmount(),
//                subscription.getServiceName(),
//                transactionDate.toString(),
//                "구독",
//                0,
//                "국민은행"
//            ));
//        }

        return transactions;
    }

    public SubscriptionDetailPanel() {
        super();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            // 화면이 보일 때마다 새로 콘텐츠 생성
            JPanel content = createContent();
            setContent(content);
        }
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // SessionManager에서 선택된 구독 정보 가져오기
        SubscriptionService subscription = SessionManager.getInstance().getSelectedSubscription();
        // Mock 거래 내역 데이터 생성 (각 Panel에서 처리)
        List<Transaction> transactions = generateMockTransactions(subscription);

        if (subscription != null) {
            // 상단 헤더
            panel.add(new PanelHeader("구독 상세보기"), BorderLayout.NORTH);

            // 상단 정보 섹션
            JPanel topSection = new JPanel(new BorderLayout());
            topSection.setBackground(Color.WHITE);
            topSection.add(new InformationSection(subscription), BorderLayout.NORTH);
            topSection.add(new TransactionHistoryList(transactions), BorderLayout.CENTER);

            panel.add(topSection, BorderLayout.CENTER);

            // 하단 버튼 섹션
            panel.add(createButtonSection(), BorderLayout.SOUTH);
        }

        return panel;
    }

    private JPanel createButtonSection() {
        JPanel buttonPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 40));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));
        buttonPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 180));

        RoundedButton editBtn = new RoundedButton("구독 해지", UIConstants.BUTTON_COLOR, e -> {
            System.out.println(((RoundedButton) e.getSource()).getText());
        });
        RoundedButton cancelBtn = new RoundedButton("연결 계좌 변경", UIConstants.BUTTON_COLOR, e -> {
            System.out.println(((RoundedButton) e.getSource()).getText());
        });

        buttonPanel.add(editBtn);
        buttonPanel.add(cancelBtn);

        return buttonPanel;
    }

}
