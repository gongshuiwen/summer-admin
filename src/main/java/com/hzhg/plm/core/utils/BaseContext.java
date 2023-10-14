package com.hzhg.plm.core.utils;

import com.hzhg.plm.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseContext {

    public static Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getId();
        }
        return null;
    }
}
