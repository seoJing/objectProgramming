package util;

import model.User;
import model.UserList;

public class AuthService {

    private final UserList userList;

    public AuthService() {
        this.userList = UserList.getInstance();
    }

    // ===================== 회원가입 =====================

    public User register(
            String id,
            String rawPassword,
            String name,
            String gender,
            int age,
            String occupation,
            String residence,
            String phoneNumber,
            boolean admin
    ) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID는 필수 입력 항목입니다.");
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new IllegalArgumentException("비밀번호는 6자 이상이어야 합니다.");
        }
        if (userList.exists(id)) {
            throw new IllegalStateException("이미 존재하는 ID입니다.");
        }

        // 비밀번호 해시 생성
        String passwordHash = PasswordUtil.hashPasswordWithIdSalt(id, rawPassword);

        User newUser = new User(
                id,
                passwordHash,
                name,
                gender,
                age,
                occupation,
                residence,
                phoneNumber,
                admin
        );

        userList.add(newUser);

        return newUser;
    }

    // ===================== 로그인 =====================

    public User login(String id, String rawPassword) {
        if (id == null || rawPassword == null) {
            throw new IllegalArgumentException("ID와 비밀번호를 모두 입력해야 합니다.");
        }

        User user = userList.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("ID 또는 비밀번호가 올바르지 않습니다.");
        }

        String inputHash = PasswordUtil.hashPasswordWithIdSalt(id, rawPassword);

        if (!inputHash.equals(user.getPasswordHash())) {
            throw new IllegalArgumentException("ID 또는 비밀번호가 올바르지 않습니다.");
        }

        return user;
    }

    // ===================== 관리자용 메서드 =====================

    public void printAllUsers() {
        for (User user : userList.getAll()) {
            System.out.println("- " + user.getId() + " (" + user.getName() + ")");
        }
    }
}
