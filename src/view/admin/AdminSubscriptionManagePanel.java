package view.admin;

import model.SubscriptionService;
import service.AdminSubscriptionService;
import util.UIConstants;
import view.layout.AdminLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminSubscriptionManagePanel extends AdminLayout {

    private JTable table;
    private DefaultTableModel model;
    private AdminSubscriptionService service;

    private JLabel lblServiceName;
    private JLabel lblAmount;
    private JLabel lblPaymentDate;
    private JLabel lblNextPayment;
    private JLabel lblUsers;
    private JLabel lblPersonalCost;

    public AdminSubscriptionManagePanel() {
        super();

        service = AdminSubscriptionService.getInstance();

        JPanel content = createContent();
        setContent(content);

        refresh();
    }

    // ================================
    // UI 생성
    // ================================
    private JPanel createContent() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // 제목
        JLabel title = new JLabel("구독 관리");
        title.setFont(UIConstants.LARGE_FONT);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.add(title, BorderLayout.NORTH);

        // 테이블 컬럼
        model = new DefaultTableModel(
                new String[]{"서비스명", "요금", "결제일", "다음 결제일", "공유 인원", "1/N 비용"},
                0
        );

        table = new JTable(model);
        table.setRowHeight(28);

        table.getSelectionModel().addListSelectionListener(e -> {
            SubscriptionService s = getSelected();
            updateInfoPanel(s);
        });

        JScrollPane scroll = new JScrollPane(table);
        panel.add(scroll, BorderLayout.CENTER);

        // 우측 정보 패널
        JPanel infoPanel = createInfoPanel();
        panel.add(infoPanel, BorderLayout.EAST);

        return panel;
    }

    // ================================
    // 우측 정보 / 수정 패널
    // ================================
    private JPanel createInfoPanel() {

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setPreferredSize(new Dimension(340, 0));
        box.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblServiceName = createInfoLabel("서비스명 : -");
        lblAmount = createInfoLabel("요금 : -");
        lblPaymentDate = createInfoLabel("결제일 : -");
        lblNextPayment = createInfoLabel("다음 결제일 : -");
        lblUsers = createInfoLabel("공유 인원 : -");
        lblPersonalCost = createInfoLabel("1/N 비용 : -");

        box.add(Box.createVerticalStrut(20));

        // 결제 처리
        JButton btnPay = new JButton("결제 처리");
        btnPay.setFont(UIConstants.NORMAL_FONT);
        btnPay.addActionListener(e -> processPayment());
        box.add(btnPay);
        box.add(Box.createVerticalStrut(10));

        // 요금 수정
        JButton btnEditPrice = new JButton("요금 수정");
        btnEditPrice.setFont(UIConstants.NORMAL_FONT);
        btnEditPrice.addActionListener(e -> editAmount());
        box.add(btnEditPrice);
        box.add(Box.createVerticalStrut(10));

        // 공유 인원 수정
        JButton btnEditUserCount = new JButton("공유 인원 변경");
        btnEditUserCount.setFont(UIConstants.NORMAL_FONT);
        btnEditUserCount.addActionListener(e -> editUserCount());
        box.add(btnEditUserCount);
        box.add(Box.createVerticalStrut(10));

        // 결제 주기 변경
        JButton btnEditPeriod = new JButton("결제 주기 변경");
        btnEditPeriod.setFont(UIConstants.NORMAL_FONT);
        btnEditPeriod.addActionListener(e -> editPeriod());
        box.add(btnEditPeriod);

        box.add(Box.createVerticalGlue());
        return box;
    }

    private JLabel createInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.NORMAL_FONT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    // ================================
    // 데이터 새로고침
    // ================================
    private void refresh() {
        model.setRowCount(0);

        List<SubscriptionService> list = service.getAll();
        for (SubscriptionService s : list) {
            model.addRow(new Object[]{
                    s.getServiceName(),
                    s.getAmount() + "원",
                    s.getPaymentDate(),
                    s.getNextPaymentDate(),
                    s.getNumberOfUsers(),
                    s.getPersonalCost() + "원"
            });
        }

        updateInfoPanel(null);
    }

    private SubscriptionService getSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return null;

        String name = (String) model.getValueAt(row, 0);
        return service.findByName(name);
    }

    private void updateInfoPanel(SubscriptionService s) {
        if (s == null) {
            lblServiceName.setText("서비스명 : -");
            lblAmount.setText("요금 : -");
            lblPaymentDate.setText("결제일 : -");
            lblNextPayment.setText("다음 결제일 : -");
            lblUsers.setText("공유 인원 : -");
            lblPersonalCost.setText("1/N 비용 : -");
            return;
        }

        lblServiceName.setText("서비스명 : " + s.getServiceName());
        lblAmount.setText("요금 : " + s.getAmount() + "원");
        lblPaymentDate.setText("결제일 : " + s.getPaymentDate());
        lblNextPayment.setText("다음 결제일 : " + s.getNextPaymentDate());
        lblUsers.setText("공유 인원 : " + s.getNumberOfUsers());
        lblPersonalCost.setText("1/N 비용 : " + s.getPersonalCost() + "원");
    }

    // ================================
    // 기능 1: 결제 처리
    // ================================
    private void processPayment() {
        SubscriptionService s = getSelected();
        if (s == null) {
            JOptionPane.showMessageDialog(this, "구독을 선택하세요.");
            return;
        }

        s.setPaymentDate(s.getNextPaymentDate());
        JOptionPane.showMessageDialog(this, "결제가 완료되었습니다.");
        refresh();
    }

    // ================================
    // 기능 2: 요금 수정
    // ================================
    private void editAmount() {
        SubscriptionService s = getSelected();
        if (s == null) return;

        String input = JOptionPane.showInputDialog(this, "새 요금 입력:", s.getAmount());
        if (input == null) return;

        try {
            int newAmount = Integer.parseInt(input);
            s.setAmount(newAmount);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "숫자를 입력하세요.");
        }
    }

    // ================================
    // 기능 3: 공유 인원 수정
    // ================================
    private void editUserCount() {
        SubscriptionService s = getSelected();
        if (s == null) return;

        String input = JOptionPane.showInputDialog(this, "공유 인원 입력:", s.getNumberOfUsers());
        if (input == null) return;

        try {
            int cnt = Integer.parseInt(input);
            if (cnt <= 0) {
                JOptionPane.showMessageDialog(this, "1 이상의 값을 입력하세요.");
                return;
            }
            s.setNumberOfUsers(cnt);
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "숫자를 입력하세요.");
        }
    }

    // ================================
    // 기능 4: 결제 주기 수정
    // ================================
    private void editPeriod() {
        SubscriptionService s = getSelected();
        if (s == null) return;

        String[] options = {"1개월", "3개월", "6개월"};
        int idx = JOptionPane.showOptionDialog(
                this, "결제 주기 선택",
                "주기 변경",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (idx == -1) return;

        int period = (idx == 0 ? 1 : idx == 1 ? 3 : 6);
        s.setSubscriptionPeriod(period);

        refresh();
    }

}
