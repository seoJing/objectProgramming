package model;

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

    // ===================== CRUD =====================

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

    public boolean exists(String id) {
        return findById(id) != null;
    }

    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    public int size() {
        return users.size();
    }

    public void clear() {
        users.clear();
    }

    
}
