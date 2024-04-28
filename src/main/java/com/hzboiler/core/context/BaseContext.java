package com.hzboiler.core.context;

import com.hzboiler.base.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseContext {

    private static final ThreadLocal<User> USER = new ThreadLocal<>();

    public static User getUser() {
        return USER.get();
    }

    public static UserDetails getUserDetails() {
        if (USER.get() == null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails user) {
                return user;
            }
        }

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
