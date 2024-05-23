package com.hzboiler.core.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class Constants {

    public static final String CODE_SYS_ADMIN = "SYS_ADMIN";
    public static final String CODE_BASE_USER = "BASE_USER";

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_SYS_ADMIN = ROLE_PREFIX + CODE_SYS_ADMIN;
    public static final String ROLE_BASE_USER = ROLE_PREFIX + CODE_BASE_USER;

    public static final GrantedAuthority AUTHORITY_ROLE_SYS_ADMIN = new SimpleGrantedAuthority(ROLE_SYS_ADMIN);
    public static final GrantedAuthority AUTHORITY_ROLE_BASE_USER = new SimpleGrantedAuthority(ROLE_BASE_USER);

    // prevent instantiation
    private Constants() {}
}
