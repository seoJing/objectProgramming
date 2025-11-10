package view.login;

import util.AuthService;
import util.SessionManager;
import model.User;

import javax.swing.*;
import java.awt.*;

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
        this.authService = new AuthService();
        initUI();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UIConstants.WHITE);

        JLabel titleLabel = new JLabel("로그인");
        titleLabel.setFont(UIConstants.LARGE_FONT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(40));
        add(titleLabel);
        add(Box.createVerticalStrut(25));

        // 아이디
        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(UIConstants.NORMAL_FONT);
        idLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        idField = new JTextField(15);
        idField.setMaximumSize(idField.getPreferredSize());
        idField.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(idLabel);
        add(idField);
        add(Box.createVerticalStrut(15));

        // 비밀번호
        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(UIConstants.NORMAL_FONT);
        pwLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField(15);
        passwordField.setMaximumSize(passwordField.getPreferredSize());
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(pwLabel);
        add(passwordField);
        add(Box.createVerticalStrut(20));

        // 에러
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UIConstants.NORMAL_FONT);
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(errorLabel);
        add(Box.createVerticalStrut(20));

        JButton loginButton = createButton("로그인");
        loginButton.addActionListener(e -> onLogin());
        add(loginButton);

        add(Box.createVerticalStrut(10));

        JButton signupButton = createButton("회원가입");
        signupButton.addActionListener(e -> openSignupDialog());
        add(signupButton);

        add(Box.createVerticalStrut(30));
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(UIConstants.NORMAL_FONT);
        btn.setFocusPainted(false);
        btn.setBackground(UIConstants.NAV_BACKGROUND_COLOR);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIConstants.NAV_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIConstants.NAV_BACKGROUND_COLOR);
            }
        });

        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void onLogin() {
        String id = idField.getText().trim();
        String pw = new String(passwordField.getPassword());

/*
로그인 성공 시 SessionManager에 사용자 정보 저장

로그인 여부/관리자 여부를 SessionManager가 판단

로그아웃하면 SessionManager가 모든 상태를 초기화

UI는 SessionManager만 보고 로그인 상태를 다룸
→ 로그인 로직이 한 곳에 모임 (Single Source of Truth)
*/
        try {
            User user = authService.login(id, pw);

            SessionManager.getInstance().login(user);
            errorLabel.setText(" ");

            if (user.isAdmin()) {
                Router.getInstance().navigateTo(Routes.ADMIN);
            } else {
                Router.getInstance().navigateTo(Routes.USER);
            }

        } catch (Exception ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    private void openSignupDialog() {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
        SignupDialog dialog = new SignupDialog(parent, authService);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
