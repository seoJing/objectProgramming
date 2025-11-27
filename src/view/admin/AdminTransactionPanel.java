package view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Account;
import model.Transaction;
import model.User;
import model.UserList;
import util.UIConstants;
import view.layout.AdminLayout;

public class AdminTransactionPanel extends AdminLayout {

    private JComboBox<User> userCombo;
    private JComboBox<Account> accountCombo;
    private JTable transactionTable;
    private DefaultTableModel tableModel;

    public AdminTransactionPanel() {
        super();
        setContent(createContent());
        loadUsers();
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // 상단 검색 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setOpaque(false);

        topPanel.add(new JLabel("회원 선택:"));
        userCombo = new JComboBox<>();
        userCombo.setPreferredSize(UIConstants.USER_COMBO_SIZE);
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

        topPanel.add(new JLabel("계좌 선택:"));
        accountCombo = new JComboBox<>();
        accountCombo.setPreferredSize(UIConstants.ACCOUNT_COMBO_SMALL_SIZE);
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

        // 중앙 테이블
        String[] columnNames = {"날짜", "거래 장소", "구분", "금액", "카테고리", "잔액"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        transactionTable = new JTable(tableModel);
        transactionTable.setRowHeight(25);

        panel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        userCombo.addActionListener(e -> {
            User selectedUser = (User) userCombo.getSelectedItem();
            if (selectedUser != null) loadAccounts(selectedUser);
        });

        accountCombo.addActionListener(e -> {
            Account selectedAccount = (Account) accountCombo.getSelectedItem();
            if (selectedAccount != null) loadTransactions(selectedAccount);
            else tableModel.setRowCount(0);
        });

        return panel;
    }

    private void loadUsers() {
        List<User> users = UserList.getInstance().getAll();
        userCombo.removeAllItems();
        for (User user : users) userCombo.addItem(user);
    }

    private void loadAccounts(User user) {
        List<Account> accounts = user.getAccountList();

        accountCombo.removeAllItems();
        for (Account acc : accounts) {
            accountCombo.addItem(acc);
        }
        if (accounts.isEmpty()) tableModel.setRowCount(0);
    }

    private void loadTransactions(Account account) {
        List<Transaction> transactions = account.getTransactionList();

        tableModel.setRowCount(0);
        for (Transaction tx : transactions) {
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
