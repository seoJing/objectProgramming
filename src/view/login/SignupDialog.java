package view.login;

import model.User;
import util.AuthService;
import util.UIConstants;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

/**
 * 회원가입 다이얼로그
 * - ID, 비밀번호, 이름, 성별, 나이, 직업, 거주지, 전화번호, 관리자 여부
 * - 가입 시 AuthService.register(...) 호출 → 내부에서 비밀번호 해시 처리
 */
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

        setSize(400, 450);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        // 제목
        JLabel titleLabel = new JLabel("회원가입");
        titleLabel.setFont(UIConstants.LARGE_FONT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // 폼
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        formPanel.add(new JLabel("아이디"));
        formPanel.add(idField);

        formPanel.add(new JLabel("비밀번호"));
        formPanel.add(pwField);

        formPanel.add(new JLabel("비밀번호 확인"));
        formPanel.add(pwConfirmField);

        formPanel.add(new JLabel("이름"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("성별"));
        formPanel.add(genderField);

        formPanel.add(new JLabel("나이"));
        formPanel.add(ageField);

        formPanel.add(new JLabel("직업"));
        formPanel.add(occupationField);

        formPanel.add(new JLabel("거주지"));
        formPanel.add(residenceField);

        formPanel.add(new JLabel("전화번호"));
        formPanel.add(phoneField);

        formPanel.add(new JLabel(" "));
        formPanel.add(adminCheck);

        add(formPanel, BorderLayout.CENTER);

        // 버튼 영역
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("가입");
        JButton cancelButton = new JButton("취소");

        submitButton.setFont(UIConstants.NORMAL_FONT);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> onSubmit());

        cancelButton.setFont(UIConstants.NORMAL_FONT);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void onSubmit() {
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

        try {
            if (!pw.equals(pw2)) {
                throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            }
            int age = Integer.parseInt(ageText);

            User newUser = authService.register(
                    id,
                    pw,          // 🔐 내부에서 해시 처리
                    name,
                    gender,
                    age,
                    occupation,
                    residence,
                    phone,
                    isAdmin
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
                    "나이는 숫자로 입력해주세요.",
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
