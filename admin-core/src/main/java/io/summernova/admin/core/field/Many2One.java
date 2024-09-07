package io.summernova.admin.core.field;

import io.summernova.admin.core.model.BaseModel;
import lombok.Getter;

/**
 * @author gongshuiwen
 */
@Getter
public final class Many2One<T extends BaseModel> {

    private final Long id; // id of record
    private final String name; // name of record
    private final T record;

    // prevent external instantiation, just for deserialization
    private Many2One() {
        this(null, null, null);
    }

    // prevent external instantiation
    private Many2One(Long id, String name, T record) {
        this.id = id;
        this.name = name;
        this.record = record;
    }

    public static <T extends BaseModel> Many2One<T> ofId(Long id) {
        return new Many2One<>(id, null, null);
    }

    public static <T extends BaseModel> Many2One<T> ofRecord(T record) {
        return new Many2One<>(record.getId(), record.getDisplayName(), record);
    }
}
