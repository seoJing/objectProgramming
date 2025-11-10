package model;

import util.PasswordUtil;   // 🔹 해시 유틸 추가 import

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private static UserList instance;
    private final List<User> users;

    private UserList() {
        this.users = new ArrayList<>();
    }

    public static UserList getInstance() {
        if (instance == null) {
            instance = new UserList();
        }
        return instance;
    }

    // ===================== 기본 CRUD =====================

    public void add(User user) {
        if (user != null) {
            users.add(user);
        }
    }

    public boolean remove(User user) {
        return users.remove(user);
    }

    public User findById(String id) {
        if (id == null) {
            return null;
        }
        for (User user : users) {
            if (id.equals(user.getId())) {
                return user;
            }
        }
        return null;
    }

    /**
     * ID + 비밀번호로 유저 찾기
     * - 여기서 password는 "raw password(사용자가 입력한 생 비밀번호)"를 받는다.
     * - 내부에서 PasswordUtil을 사용해 해시로 변환 후,
     *   User에 저장된 해시값(user.getPassword())와 비교한다.
     */
    public User findByIdAndPassword(String id, String rawPassword) {
        if (id == null || rawPassword == null) {
            return null;
        }

        // 🔹 입력받은 비밀번호를 같은 방식으로 해싱
        String hashedInput = PasswordUtil.hashPasswordWithIdSalt(id, rawPassword);

        for (User user : users) {
            // user.getPassword()에는 이미 "해시된 비밀번호"가 들어 있다고 가정
            if (id.equals(user.getId()) && hashedInput.equals(user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    public int size() {
        return users.size();
    }

    public boolean exists(String id) {
        return findById(id) != null;
    }

    public void clear() {
        users.clear();
    }
}
