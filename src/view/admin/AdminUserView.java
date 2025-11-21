package view.admin;

import model.User;
import model.Account;
import model.UserList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminUserView extends JFrame {

    private JTable userTable;
    private JTable accountTable;

    public AdminUserView(UserList userList) {
        setTitle("관리자 - 사용자 조회");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // ===========================
        // Left: 사용자 목록 테이블
        // ===========================
        String[] userColumns = {"ID", "이름", "성별", "나이", "직업", "전화번호", "계좌수"};
        DefaultTableModel userModel = new DefaultTableModel(userColumns, 0);
        userTable = new JTable(userModel);

        // 사용자 목록 채우기
        for (User u : userList.getAll()) {
            userModel.addRow(new Object[]{
                    u.getId(),
                    u.getName(),
                    u.getGender(),
                    u.getAge(),
                    u.getOccupation(),
                    u.getPhoneNumber(),
                    u.getAccountList().size()
            });
        }

        add(new JScrollPane(userTable));

        // ===========================
        // Right: 선택된 사용자 계좌 목록
        // ===========================
        String[] accColumns = {"계좌번호", "현재 잔액", "거래내역 개수"};
        DefaultTableModel accountModel = new DefaultTableModel(accColumns, 0);
        accountTable = new JTable(accountModel);

        add(new JScrollPane(accountTable));

        // ===========================
        // 사용자 테이블 클릭 이벤트
        // ===========================
        userTable.getSelectionModel().addListSelectionListener(e -> {
            int row = userTable.getSelectedRow();
            if (row == -1) return;

            String userId = (String) userTable.getValueAt(row, 0);
            User selectedUser = userList.findById(userId);   // 수정된 부분

            // 계좌 테이블 초기화
            accountModel.setRowCount(0);

            if (selectedUser != null) {
                for (Account acc : selectedUser.getAccountList()) {
                    accountModel.addRow(new Object[]{
                            acc.getAccountNumber(),
                            acc.getCurrentBalance(),                 // 수정된 부분
                            acc.getTransactionList().size()          // 수정된 부분
                    });
                }
            }
        });

        setVisible(true);
    }
}
