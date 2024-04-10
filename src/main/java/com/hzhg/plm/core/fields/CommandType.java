package com.hzhg.plm.core.fields;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CommandType {
    ADD(0), // For Many2Many
    CREATE(1), // For One2Many
    REMOVE(2), // For Many2Many
    DELETE(3), // For One2Many
    REPLACE(4), // For Many2Many
    UPDATE(5); // For One2Many

    @JsonValue
    int code;

    public static CommandType of(int code) {
        for (CommandType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw new IllegalArgumentException("Invalid command type code: " + code);
    }
}
