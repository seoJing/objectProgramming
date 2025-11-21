package view.admin;

import model.SubscriptionService;
import model.User;
import model.UserList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminSubscriptionManagePanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public AdminSubscriptionManagePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel title = new JLabel("구독 서비스 관리", SwingConstants.CENTER);
        title.setFont(new Font("Pretendard", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // ======= 테이블 설정 =======
        String[] columns = {"사용자ID", "서비스명", "구독료", "결제일", "인원수", "다음 결제일"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        loadSubscriptionData();

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ======= 버튼 패널 =======
        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("추가");
        JButton btnEdit = new JButton("수정");
        JButton btnDelete = new JButton("삭제");

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        // ===== 버튼 기능 =====
        btnAdd.addActionListener(e -> addSubscription());
        btnEdit.addActionListener(e -> editSubscription());
        btnDelete.addActionListener(e -> deleteSubscription());
    }

    // =============================================================
    // 데이터 로드
    // =============================================================
    private void loadSubscriptionData() {
        model.setRowCount(0);
        for (User u : UserList.getInstance().getAll()) {
            for (SubscriptionService s : u.getLedger().getSubscriptionList()) {
                model.addRow(new Object[]{
                        s.getUserId(),
                        s.getServiceName(),
                        s.getAmount(),
                        s.getPaymentDate(),
                        s.getNumberOfUsers(),
                        s.getNextPaymentDate()
                });
            }
        }
    }

    // =============================================================
    // 구독 추가
    // =============================================================
    private void addSubscription() {
        JTextField userId = new JTextField();
        JTextField svcName = new JTextField();
        JTextField amount = new JTextField();
        JTextField payDate = new JTextField("2024-01-01");
        JTextField period = new JTextField("1");
        JTextField users = new JTextField("1");

        Object[] msg = {
                "사용자 ID:", userId,
                "서비스명:", svcName,
                "금액:", amount,
                "결제일(yyyy-MM-dd):", payDate,
                "결제주기(개월):", period,
                "공유 인원수:", users
        };

        int result = JOptionPane.showConfirmDialog(this, msg, "구독 추가", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        User u = UserList.getInstance().findById(userId.getText());
        if (u == null) {
            JOptionPane.showMessageDialog(this, "해당 ID의 사용자가 존재하지 않습니다.");
            return;
        }

        SubscriptionService sub = new SubscriptionService(
                svcName.getText(),
                Integer.parseInt(amount.getText()),
                payDate.getText(),
                userId.getText(),
                Integer.parseInt(period.getText()),
                Integer.parseInt(users.getText())
        );

        u.getLedger().addSubscription(sub);
        loadSubscriptionData();
    }

    // =============================================================
    // 구독 수정
    // =============================================================
    private void editSubscription() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "수정할 항목을 선택하세요.");
            return;
        }

        String userId = (String) model.getValueAt(row, 0);
        User u = UserList.getInstance().findById(userId);

        if (u == null) return;

        SubscriptionService target = u.getLedger().getSubscriptionList().get(row);

        JTextField svcName = new JTextField(target.getServiceName());
        JTextField amount = new JTextField(String.valueOf(target.getAmount()));
        JTextField payDate = new JTextField(target.getPaymentDate());
        JTextField period = new JTextField(String.valueOf(target.getSubscriptionPeriod()));
        JTextField users = new JTextField(String.valueOf(target.getNumberOfUsers()));

        Object[] msg = {
                "서비스명:", svcName,
                "금액:", amount,
                "결제일:", payDate,
                "기간:", period,
                "인원수:", users
        };

        int result = JOptionPane.showConfirmDialog(this, msg, "구독 수정", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) return;

        target.setPaymentDate(payDate.getText());
        target.setAmount(Integer.parseInt(amount.getText()));
        target.setSubscriptionPeriod(Integer.parseInt(period.getText()));
        target.setNumberOfUsers(Integer.parseInt(users.getText()));

        loadSubscriptionData();
    }

    // =============================================================
    // 구독 삭제
    // =============================================================
    private void deleteSubscription() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 항목을 선택하세요.");
            return;
        }

        String userId = (String) model.getValueAt(row, 0);
        User u = UserList.getInstance().findById(userId);

        if (u == null) return;

        SubscriptionService target = u.getLedger().getSubscriptionList().get(row);
        u.getLedger().removeSubscription(target);

        loadSubscriptionData();
    }
}
