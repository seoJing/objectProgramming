package model;

import java.util.ArrayList;
import java.util.List;

public class AdminUserList {
    private static AdminUserList instance;
    private List<AdminUser> adminUsers;

    private AdminUserList() {
        this.adminUsers = new ArrayList<>();
        this.adminUsers.add(new AdminUser("admin", "1234", "총관리자"));
    }

    public static AdminUserList getInstance() {
        if (instance == null) {
            instance = new AdminUserList();
        }
        return instance;
    }

    // 로그인 시 사용
    public AdminUser findByIdAndPassword(String id, String password) {
        if (id == null || password == null) return null;

        for (AdminUser admin : adminUsers) {
            if (id.equals(admin.getAdminId()) && password.equals(admin.getPassword())) {
                return admin;
            }
        }
        return null;
    }

    // ID로 관리자 찾기 (Notice에서 사용)
    public AdminUser findById(String id) {
        if (id == null) return null;

        for (AdminUser admin : adminUsers) {
            if (id.equals(admin.getAdminId())) {
                return admin;
            }
        }
        return null;
    }
}
