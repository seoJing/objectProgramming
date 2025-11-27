package util;

import model.User;
import model.UserList;

public class AuthService {

    private final UserList userList;

    public AuthService() {
        this.userList = UserList.getInstance();
    }

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

        if (rawPassword == null || rawPassword.isBlank()) {
                throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }

        // 비밀번호 검증 (6자 이상 + 대문자 + 소문자 + 숫자 + 특수문자)
        String passwordPattern =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|\\[\\]:;\"'<>,.?/]).{6,}$";

        if (rawPassword == null || !rawPassword.matches(passwordPattern)) {
            throw new IllegalArgumentException(
                "비밀번호는 6자 이상이며, 대문자/소문자/숫자/특수문자를 모두 포함해야 합니다."
                );
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

        System.out.println("✓ [로그인] " + id + " (" + user.getName() + ")");
        return user;
    }

    public void printAllUsers() {
        for (User user : userList.getAll()) {
            System.out.println("- " + user.getId() + " (" + user.getName() + ")");
        }
    }
}
