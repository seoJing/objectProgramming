package util;

import model.User;
import model.UserList;

/**
 * 로그인 / 회원가입 로직 담당
 * - UserList(싱글톤)를 통해 유저 데이터를 관리
 * - 비밀번호는 SHA-256 해시로 저장 및 비교
 */
public class AuthService {

    private final UserList userList;

    // 싱글톤 UserList 사용
    public AuthService() {
        this.userList = UserList.getInstance();
    }

    /**
     * 회원가입 (비밀번호는 단순 길이로만 검증 - 6자리이상 )
     */
    public User register(
            String id,
            String rawPassword,
            String name,
            String gender,
            int age,
            String occupation,
            String residence,
            String phoneNumber,
            boolean isAdmin
    ) {
        // ====== 입력값 검증 ======
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID는 필수 입력 항목입니다.");
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new IllegalArgumentException("비밀번호는 6자 이상이어야 합니다."); 
        }
        if (userList.exists(id)) {
            throw new IllegalStateException("이미 존재하는 ID입니다.");
        }

        // ====== 비밀번호 해시 처리 ======
        String hashedPassword = PasswordUtil.hashPasswordWithIdSalt(id, rawPassword);

        // ====== User 객체 생성 (password 자리에 해시 저장) ======
        User newUser = new User(
                id,
                hashedPassword,  // 평문 대신 해시값 !
                name,
                gender,
                age,
                occupation,
                residence,
                phoneNumber,
                isAdmin
        );

        // ====== UserList에 추가 ======
        userList.add(newUser);

        return newUser;
    }

    /**
     * 로그인
     */
    public User login(String id, String rawPassword) {
        if (id == null || rawPassword == null) {
            throw new IllegalArgumentException("ID와 비밀번호를 모두 입력해야 합니다.");
        }

        // UserList에 위임해서 해시 비교
        User user = userList.findByIdAndPassword(id, rawPassword);
        if (user == null) {
            // findByIdAndPassword가 null이면 ID가 없거나 비번이 틀린 경우 둘 다 포함
            throw new IllegalArgumentException("ID 또는 비밀번호가 올바르지 않습니다.");
        }

        return user;
    }

    /**
     * 전체 유저 출력 (디버깅/관리자용)
     */
    public void printAllUsers() {
        for (User u : userList.getAll()) {
            System.out.println("- " + u.getId() + " (" + u.getName() + ")");
        }
    }
}
