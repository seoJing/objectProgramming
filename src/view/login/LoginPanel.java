package view.login;

import model.User;
import util.AuthService;
import util.Router;
import util.Routes;
import util.UIConstants;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Color;

/**
 * 로그인 화면
 * - ID / 비밀번호 입력
 * - 로그인 버튼: AuthService.login() 호출 (내부에서 해시 비교)
 * - 회원가입 버튼: SignupDialog 띄우기
 */
public class LoginPanel extends JPanel {

    private final AuthService authService;

    private JTextField idField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginPanel() {
        this.authService = new AuthService(); // UserList 싱글톤 사용

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 제목
        JLabel titleLabel = new JLabel("로그인");
        titleLabel.setFont(UIConstants.LARGE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(Box.createVerticalStrut(40));
        add(titleLabel);
        add(Box.createVerticalStrut(20));

        // ID 라벨 + 필드
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(UIConstants.NORMAL_FONT);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(idLabel);

        idField = new JTextField(15);
        idField.setMaximumSize(idField.getPreferredSize());
        idField.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(idField);
        add(Box.createVerticalStrut(10));

        // 비밀번호 라벨 + 필드
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(UIConstants.NORMAL_FONT);
        pwLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(pwLabel);

        passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(passwordField);
        add(Box.createVerticalStrut(10));

        // 에러 메시지
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UIConstants.NORMAL_FONT);
        errorLabel.setForeground(Color.RED);       // 그냥 빨간색 사용
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(errorLabel);
        add(Box.createVerticalStrut(10));

        // 로그인 버튼
        JButton loginButton = new JButton("로그인");
        loginButton.setFont(UIConstants.NORMAL_FONT);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> onLogin());
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });
        add(loginButton);
        add(Box.createVerticalStrut(10));

        // 회원가입 버튼
        JButton signupButton = new JButton("회원가입");
        signupButton.setFont(UIConstants.NORMAL_FONT);
        signupButton.setFocusPainted(false);
        signupButton.setBorderPainted(false);
        signupButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
        signupButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupButton.addActionListener(e -> openSignupDialog());
        signupButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signupButton.setBackground(UIConstants.NAV_HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signupButton.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });
        add(signupButton);

        add(Box.createVerticalStrut(40));
    }

    private void onLogin() {
        String id = idField.getText().trim();
        String pw = new String(passwordField.getPassword());

        try {
            User user = authService.login(id, pw);  // 🔐 여기서 해시 비교 포함

            errorLabel.setText(" ");
            JOptionPane.showMessageDialog(
                    this,
                    user.getName() + "님, 환영합니다!",
                    "로그인 성공",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // 관리자/일반 유저에 따라 다른 화면으로 이동
            if (user.isAdmin()) {
                Router.getInstance().navigateTo(Routes.ADMIN);
            } else {
                Router.getInstance().navigateTo(Routes.USER);
            }

        } catch (Exception ex) {
            errorLabel.setText(ex.getMessage());
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "로그인 실패",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void openSignupDialog() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        SignupDialog dialog = new SignupDialog(parent, authService);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
