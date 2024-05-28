package com.hzboiler.erp.core.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.hzboiler.erp.core.security.Constants.*;

/**
 * Data access authority enumerations
 *
 * @author gongshuiwen
 */
@Getter
@AllArgsConstructor
public enum DataAccessAuthority {

    SELECT(AUTHORITY_SELECT),
    CREATE(AUTHORITY_CREATE),
    UPDATE(AUTHORITY_UPDATE),
    DELETE(AUTHORITY_DELETE);

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
