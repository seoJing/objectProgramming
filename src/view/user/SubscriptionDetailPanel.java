package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Account;
import model.SubscriptionService;
import model.Transaction;
import model.User;
import util.Router;
import util.Routes;
import util.SessionManager;
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

        // 구독명의 기본 부분만 추출 (예: "넷플릭스_프리미엄" → "넷플릭스")
        String baseName = subscription.getServiceName().split("[_\\-]")[0];

        // 모든 계좌에서 해당 구독의 거래 필터링
        for (Account account : user.getAccountList()) {
            for (Transaction transaction : account.getTransactionList()) {
                // 거래 위치가 구독 서비스명 또는 기본명과 일치하는 경우
                if (transaction.getLocation().equals(subscription.getServiceName()) ||
                    transaction.getLocation().equals(baseName)) {
                    transactions.add(transaction);
                }
            }
        }

        // 날짜 순서로 정렬 (최신순)
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
        // 실제 거래 내역 데이터 조회
        List<Transaction> transactions = getSubscriptionTransactions(subscription);

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
            // 구독 해지 처리
            System.out.println("[구독 해지 시작] " + subscription.getServiceName());
            System.out.println("해지 전 구독 개수: " + user.getLedger().getSubscriptionList().size());
            System.out.println("대상 구독: " + subscription);

            boolean removed = user.getLedger().getSubscriptionList().remove(subscription);
            System.out.println("제거 결과: " + removed);
            System.out.println("해지 후 구독 개수: " + user.getLedger().getSubscriptionList().size());

            // 구독 페이지로 이동
            Router.getInstance().navigateUser(Routes.SUBSCRIPTION);
        });
        RoundedButton cancelBtn = new RoundedButton("연결 계좌 변경", UIConstants.BUTTON_COLOR, e -> {
            System.out.println(((RoundedButton) e.getSource()).getText());
        });

        buttonPanel.add(editBtn);
        buttonPanel.add(cancelBtn);

        return buttonPanel;
    }

}
