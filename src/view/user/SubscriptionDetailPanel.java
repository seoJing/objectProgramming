package view.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import model.Account;
import model.SubscriptionService;
import model.Transaction;
import model.TransactionType;
import model.User;
import util.Router;
import util.Routes;
import util.SessionManager;
import util.SubscriptionSavingsUtil;
import util.UIConstants;
import view.layout.UserLayout;
import view.user.shared.component.ImageLoader;
import view.user.shared.component.PanelHeader;

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
            centerSection.add(new SubscriptionInformationSection(subscription), BorderLayout.NORTH);

            // 거래 내역
            centerSection.add(new SubscriptionTransactionHistoryList(transactions), BorderLayout.CENTER);

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

        SubscriptionRoundedButton editBtn = new SubscriptionRoundedButton("구독 해지", UIConstants.BUTTON_COLOR, e -> {
            System.out.println("[구독 해지 시작] " + subscription.getServiceName());
            System.out.println("해지 전 구독 개수: " + user.getLedger().getSubscriptionList().size());
            System.out.println("대상 구독: " + subscription);

            boolean removed = user.getLedger().getSubscriptionList().remove(subscription);
            System.out.println("제거 결과: " + removed);
            System.out.println("해지 후 구독 개수: " + user.getLedger().getSubscriptionList().size());

            String previousRoute = SessionManager.getInstance().getPreviousRoute();
            Router.getInstance().navigateUser(previousRoute);
        });
        SubscriptionRoundedButton cancelBtn = new SubscriptionRoundedButton("연결 계좌 변경", UIConstants.BUTTON_COLOR, e -> {
            System.out.println(((SubscriptionRoundedButton) e.getSource()).getText());
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

    /**
     * 구독 서비스 정보(서비스명, 금액)를 표시하는 패널 컴포넌트
     * 데이터는 외부에서 주입받고, UI만 렌더링함
     */
    private static class SubscriptionInformationSection extends JPanel {

        public SubscriptionInformationSection(SubscriptionService subscription) {
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

    /**
     * 거래 내역 리스트를 표시하는 스크롤 가능한 패널 컴포넌트
     * 데이터는 외부에서 주입받고, UI만 렌더링함
     */
    private static class SubscriptionTransactionHistoryList extends JScrollPane {

        public SubscriptionTransactionHistoryList(List<Transaction> transactions) {
            JPanel listPanel = new JPanel();
            listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
            listPanel.setBackground(java.awt.Color.WHITE);
            listPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));

            if (transactions.isEmpty()) {
                listPanel.add(Box.createVerticalGlue());

                JLabel placeholderLabel = new JLabel("거래내역이 없습니다");
                placeholderLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
                placeholderLabel.setForeground(new Color(150, 150, 150));
                placeholderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                listPanel.add(placeholderLabel);

                listPanel.add(Box.createVerticalGlue());
            } else {
                for (Transaction transaction : transactions) {
                    SubscriptionTransactionItem itemPanel = new SubscriptionTransactionItem(transaction);
                    listPanel.add(itemPanel);
                    listPanel.add(Box.createVerticalStrut(8));
                }
            }

            setViewportView(listPanel);
            setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            setBorder(null);
            getVerticalScrollBar().setUnitIncrement(10);
            setPreferredSize(new Dimension(getPreferredSize().width, 280));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        }
    }

    /**
     * 거래 항목을 표시하는 패널 컴포넌트
     */
    private static class SubscriptionTransactionItem extends JPanel {

        public SubscriptionTransactionItem(Transaction transaction) {
            super(new BorderLayout());

            // 카드 스타일
            setBackground(UIConstants.CARD_BG);
            setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            setOpaque(false);

            // ===== 왼쪽: 날짜 / 계좌 / 카테고리 · 메모 =====
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.setOpaque(false);

            // 1) 날짜 / 시간 (Transaction.getDate() 는 이미 포맷된 문자열이라고 가정)
            JLabel dateLabel = new JLabel(transaction.getDate());
            dateLabel.setFont(UIConstants.SMALL_FONT);
            dateLabel.setForeground(UIConstants.TEXT_MUTED);
            leftPanel.add(dateLabel);

            // 2) 은행 / 계좌번호 (SessionManager 의 선택 계좌 사용)
            Account acc = SessionManager.getInstance().getSelectedAccount();
            String bankLine = "";
            if (acc != null) {
                bankLine = acc.getBank() + " / " + acc.getAccountNumber();
            }
            JLabel accountLabel = new JLabel(bankLine);
            accountLabel.setFont(UIConstants.NORMAL_FONT);
            accountLabel.setForeground(UIConstants.TEXT_DEFAULT);
            leftPanel.add(accountLabel);

            // 3) 카테고리 · 메모
            String extra = "카테고리: " + safe(transaction.getCategory());
            String memo = safe(transaction.getMemo());
            if (!memo.isEmpty()) {
                extra += "  ·  메모: " + memo;
            }
            JLabel extraLabel = new JLabel(extra);
            extraLabel.setFont(UIConstants.SMALL_FONT);
            extraLabel.setForeground(UIConstants.TEXT_MUTED);
            leftPanel.add(extraLabel);

            add(leftPanel, BorderLayout.WEST);

            // ===== 오른쪽: 금액 (+초록 / -검정) =====
            boolean deposit = (transaction.getType() == TransactionType.INCOME);
            String sign = deposit ? "+" : "-";
            int absAmount = Math.abs(transaction.getAmount());

            JLabel amountLabel = new JLabel(sign + UIConstants.money(absAmount) + "원");
            amountLabel.setFont(UIConstants.NORMAL_FONT.deriveFont(Font.BOLD));
            amountLabel.setForeground(deposit ? UIConstants.POS_GREEN : UIConstants.TEXT_DEFAULT);

            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            rightPanel.add(amountLabel, BorderLayout.EAST);

            add(rightPanel, BorderLayout.EAST);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            super.paintComponent(g);
        }

        private static String safe(String s) {
            return (s == null) ? "" : s;
        }
    }

    /**
     * Rounded rectangle 스타일의 커스텀 버튼 컴포넌트
     * 데이터는 외부에서 주입받고, UI만 렌더링함
     */
    private static class SubscriptionRoundedButton extends JButton {

        public SubscriptionRoundedButton(String text, Color bgColor, ActionListener onClickListener) {
            super(text);
            initButton(bgColor);
            if (onClickListener != null) {
                addActionListener(onClickListener);
            }
        }

        private void initButton(Color bgColor) {
            setFont(new Font(UIConstants.FONT_FAMILY, Font.BOLD, 14));
            setForeground(Color.WHITE);
            setBackground(bgColor);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            setPreferredSize(new Dimension(150, 44));
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
            g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            super.paintComponent(g);
        }
    }

}
