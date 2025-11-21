package view.admin;

import model.Account;
import model.Transaction;
import model.User;
import model.UserList;
import service.AccountService;
import view.layout.AdminLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminTransactionPanel extends AdminLayout {

    private JComboBox<User> userCombo;
    private JComboBox<Account> accountCombo;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public AdminTransactionPanel() {
        super();
        setContent(createContent());
        loadUsers(); // 화면이 켜지면 유저 목록부터 로드
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // 1. 상단 검색 패널 (유저 선택 -> 계좌 선택)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setOpaque(false);

        // 유저 콤보박스
        topPanel.add(new JLabel("회원 선택:"));
        userCombo = new JComboBox<>();
        userCombo.setPreferredSize(new Dimension(150, 30));
        // 콤보박스에 객체 주소 대신 '이름(ID)'가 나오도록 설정
        userCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof User) {
                    User u = (User) value;
                    setText(u.getName() + " (" + u.getId() + ")");
                }
                return this;
            }
        });
        topPanel.add(userCombo);

        // 계좌 콤보박스
        topPanel.add(new JLabel("계좌 선택:"));
        accountCombo = new JComboBox<>();
        accountCombo.setPreferredSize(new Dimension(220, 30));
        // 콤보박스에 '은행명 - 계좌번호'가 나오도록 설정
        accountCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Account) {
                    Account a = (Account) value;
                    setText(a.getBank() + " - " + a.getAccountNumber());
                }
                return this;
            }
        });
        topPanel.add(accountCombo);

        panel.add(topPanel, BorderLayout.NORTH);

        // 2. 중앙 테이블 (거래 내역)
        String[] columnNames = {"날짜", "거래 장소", "구분", "금액", "카테고리", "잔액"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override // 테이블 수정 방지
            public boolean isCellEditable(int row, int column) { return false; }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(25);
        transactionTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        // 3. 이벤트 리스너 (동작 연결)

        // 유저 선택 시 -> 해당 유저의 계좌 목록 로드
        userCombo.addActionListener(e -> {
            User selectedUser = (User) userCombo.getSelectedItem();
            if (selectedUser != null) {
                loadAccounts(selectedUser);
            }
        });

        // 계좌 선택 시 -> 해당 계좌의 거래 내역 로드
        accountCombo.addActionListener(e -> {
            Account selectedAccount = (Account) accountCombo.getSelectedItem();
            if (selectedAccount != null) {
                loadTransactions(selectedAccount);
            } else {
                // 계좌가 없으면 테이블 초기화
                tableModel.setRowCount(0);
            }
        });

        return panel;
    }

    // 전체 유저 목록 불러오기
    private void loadUsers() {
        List<User> users = UserList.getInstance().getAll();
        userCombo.removeAllItems();
        for (User user : users) {
            userCombo.addItem(user);
        }
    }

    // 선택된 유저의 계좌 목록 불러오기
    private void loadAccounts(User user) {
        // AccountService 사용 (또는 user.getAccountList() 직접 사용 가능)
        List<Account> accounts = AccountService.getInstance().getAccounts(user);

        accountCombo.removeAllItems(); // 기존 항목 삭제
        for (Account acc : accounts) {
            accountCombo.addItem(acc);
        }

        // 계좌가 하나도 없으면 테이블 비우기
        if (accounts.isEmpty()) {
            tableModel.setRowCount(0);
        }
    }

    // 선택된 계좌의 거래 내역 불러오기
    private void loadTransactions(Account account) {
        List<Transaction> transactions = AccountService.getInstance().getTransactions(account);

        tableModel.setRowCount(0); // 기존 데이터 삭제

        for (Transaction tx : transactions) {
            // 테이블 행 추가
            tableModel.addRow(new Object[]{
                    tx.getDate(),
                    tx.getLocation(),
                    tx.getType().toString(),
                    String.format("%,d원", tx.getAmount()),
                    tx.getCategory(),
                    String.format("%,d원", tx.getBalanceAfter())
            });
        }
    }
}
