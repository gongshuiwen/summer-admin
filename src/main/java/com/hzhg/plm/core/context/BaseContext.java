package com.hzhg.plm.core.context;

import com.hzhg.plm.entity.User;

public class BaseContext {

    private static final ThreadLocal<User> USER = new ThreadLocal<>();

    public static User getUser() {
        return USER.get();
    }

    public static void setUser(User user) {
        USER.set(user);
    }

    public static void removeUser() {
        USER.remove();
    }

    public static Long getCurrentUserId() {
        User user = USER.get();
        if (user != null && user.getId() != null) {
            return user.getId();
        }

        return 0L;
    }
}
