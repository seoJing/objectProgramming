package model;

import java.util.ArrayList;
import java.util.List;

public class UserList {
    private static UserList instance;
    private List<User> users;

    private UserList() {
        this.users = new ArrayList<>();
    }

    public static UserList getInstance() {
        if (instance == null) {
            instance = new UserList();
        }
        return instance;
    }

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

    public User findByIdAndPassword(String id, String password) {
        if (id == null || password == null) {
            return null;
        }
        for (User user : users) {
            if (id.equals(user.getId()) && password.equals(user.getPassword())) {
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
