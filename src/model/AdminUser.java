package model;

public class AdminUser {
    private String adminId;
    private String password;
    private String name;

    // 생성자
    public AdminUser(String adminId, String password, String name) {
        this.adminId = adminId;
        this.password = password;
        this.name = name;
    }

    // Getters
    public String getAdminId() { return adminId; }
    public String getPassword() { return password; }
    public String getName() { return name; }
}
