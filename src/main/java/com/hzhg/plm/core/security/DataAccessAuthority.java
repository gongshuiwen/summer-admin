package com.hzhg.plm.core.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataAccessAuthority {

    SELECT("SELECT"),
    CREATE("CREATE"),
    UPDATE("UPDATE"),
    DELETE("DELETE");

    private final String name;
}
