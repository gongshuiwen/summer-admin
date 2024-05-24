package com.hzboiler.core.fields;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author gongshuiwen
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CommandType {

    // Many2Many field command types
    ADD(0),
    REMOVE(1),
    REPLACE(2),

    // One2Many field command types
    CREATE(3),
    DELETE(4),
    UPDATE(5);

    @JsonValue
    int code;

    /**
     * Get CommandType enum by code.
     * @param code command type code
     * @return CommandType enum
     * @throws IllegalArgumentException Invalid command type code
     */
    public static CommandType of(int code) {
        for (CommandType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid command type code: " + code);
    }
}
