package com.hzboiler.core.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataAccessAuthority {

    SELECT("SELECT"),
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    public static final String AUTHORITY_SELECT = "SELECT";
    public static final String AUTHORITY_CREATE = "CREATE";
    public static final String AUTHORITY_UPDATE = "UPDATE";
    public static final String AUTHORITY_DELETE = "DELETE";

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
