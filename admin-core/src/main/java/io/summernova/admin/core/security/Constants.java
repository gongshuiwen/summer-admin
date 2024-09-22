package io.summernova.admin.core.security;

import io.summernova.admin.core.security.authorization.BaseAuthority;
import io.summernova.admin.core.security.authorization.SimpleAuthority;

/**
 * @author gongshuiwen
 */
public final class Constants {

    public static final String ROLE_PREFIX = "ROLE_";

    public static final String CODE_SYS_ADMIN = "SYS_ADMIN";
    public static final String CODE_BASE_USER = "BASE_USER";

    public static final String ROLE_SYS_ADMIN = ROLE_PREFIX + CODE_SYS_ADMIN;
    public static final String ROLE_BASE_USER = ROLE_PREFIX + CODE_BASE_USER;

    public static final BaseAuthority GRANTED_AUTHORITY_ROLE_SYS_ADMIN = SimpleAuthority.of(ROLE_SYS_ADMIN);
    public static final BaseAuthority GRANTED_AUTHORITY_ROLE_BASE_USER = SimpleAuthority.of(ROLE_BASE_USER);

    public static final String AUTHORITY_SELECT = "SELECT";
    public static final String AUTHORITY_CREATE = "CREATE";
    public static final String AUTHORITY_UPDATE = "UPDATE";
    public static final String AUTHORITY_DELETE = "DELETE";

    public static final String AUTHORITY_SELECT_CODE_PREFIX = "S:";
    public static final String AUTHORITY_CREATE_CODE_PREFIX = "C:";
    public static final String AUTHORITY_UPDATE_CODE_PREFIX = "U:";
    public static final String AUTHORITY_DELETE_CODE_PREFIX = "D:";

    // prevent instantiation
    private Constants() {
    }
}
