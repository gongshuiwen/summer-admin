package com.hzhg.plm.core.fields;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CommandType {
    ADD(0),
    CREATE(1),
    REMOVE(2),
    REMOVE_ALL(3),
    DELETE(4),
    DELETE_ALL(5),
    REPLACE(6),
    UPDATE(7);

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
