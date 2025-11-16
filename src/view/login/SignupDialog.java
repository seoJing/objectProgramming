package view.login;

import model.User;
import util.AuthService;
import util.UIConstants;

import javax.swing.*;
import java.awt.*;

public class SignupDialog extends JDialog {

    private final AuthService authService;

    private JTextField idField;
    private JPasswordField pwField;
    private JPasswordField pwConfirmField;
    private JTextField nameField;
    private JTextField genderField;
    private JTextField ageField;
    private JTextField occupationField;
    private JTextField residenceField;
    private JTextField phoneField;
    private JCheckBox adminCheck;

    public SignupDialog(Frame owner, AuthService authService) {
        super(owner, "회원가입", true);
        this.authService = authService;

        setSize(420, 480);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        // ====================== Title ======================
        JLabel titleLabel = new JLabel("회원가입");
        titleLabel.setFont(UIConstants.LARGE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // ====================== Form Panel ======================
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        idField = new JTextField();
        pwField = new JPasswordField();
        pwConfirmField = new JPasswordField();
        nameField = new JTextField();
        genderField = new JTextField();
        ageField = new JTextField();
        occupationField = new JTextField();
        residenceField = new JTextField();
        phoneField = new JTextField();
        adminCheck = new JCheckBox("관리자 계정 여부");

        addRow(formPanel, "아이디", idField);
        addRow(formPanel, "비밀번호", pwField);
        addRow(formPanel, "비밀번호 확인", pwConfirmField);
        addRow(formPanel, "이름", nameField);
        addRow(formPanel, "성별", genderField);
        addRow(formPanel, "나이", ageField);
        addRow(formPanel, "직업", occupationField);
        addRow(formPanel, "거주지", residenceField);
        addRow(formPanel, "전화번호", phoneField);

        formPanel.add(new JLabel(" "));
        formPanel.add(adminCheck);

        add(formPanel, BorderLayout.CENTER);

        // ====================== Buttons ======================
        JPanel buttonPanel = new JPanel();

        JButton submitButton = createButton("가입");
        JButton cancelButton = createButton("취소");

        submitButton.addActionListener(e -> onSubmit());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addRow(JPanel panel, String labelText, JComponent component) {
        panel.add(new JLabel(labelText));
        panel.add(component);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.NORMAL_FONT);
        btn.setFocusPainted(false);
        btn.setBackground(UIConstants.NAV_BACKGROUND_COLOR);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIConstants.NAV_HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });

        return btn;
    }

    private void onSubmit() {
        try {
            String id = idField.getText().trim();
            String pw = new String(pwField.getPassword());
            String pw2 = new String(pwConfirmField.getPassword());
            String name = nameField.getText().trim();
            String gender = genderField.getText().trim();
            String ageText = ageField.getText().trim();
            String occupation = occupationField.getText().trim();
            String residence = residenceField.getText().trim();
            String phone = phoneField.getText().trim();
            boolean isAdmin = adminCheck.isSelected();

            if (!pw.equals(pw2)) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }

            int age = Integer.parseInt(ageText);

            User newUser = authService.register(
                    id, pw, name, gender, age, occupation, residence, phone, isAdmin
            );

            JOptionPane.showMessageDialog(
                    this,
                    "회원가입이 완료되었습니다.\nID: " + newUser.getId(),
                    "가입 성공",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "나이는 숫자로 입력해야 합니다.",
                    "입력 오류",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "회원가입 실패",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
