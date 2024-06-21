package com.hzboiler.erp.core.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.hzboiler.erp.core.security.Constants.*;

/**
 * Model access authority enumerations
 *
 * @author gongshuiwen
 */
@Getter
@AllArgsConstructor
public enum ModelAccessType {

    SELECT(AUTHORITY_SELECT, AUTHORITY_SELECT_CODE_PREFIX),
    CREATE(AUTHORITY_CREATE, AUTHORITY_CREATE_CODE_PREFIX),
    UPDATE(AUTHORITY_UPDATE, AUTHORITY_UPDATE_CODE_PREFIX),
    DELETE(AUTHORITY_DELETE, AUTHORITY_DELETE_CODE_PREFIX);

    private final String name;
    private final String prefix;

    @Override
    public String toString() {
        return name;
    }
}
