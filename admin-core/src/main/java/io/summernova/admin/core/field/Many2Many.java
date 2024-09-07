package io.summernova.admin.core.field;

import io.summernova.admin.core.model.BaseModel;

import java.util.List;

/**
 * @author gongshuiwen
 */
public final class Many2Many<T extends BaseModel> extends X2Many<T> {

    // prevent external instantiation, just for deserialization
    private Many2Many() {
        super(null, null);
    }

    // prevent external instantiation
    private Many2Many(List<Command<T>> commands, List<T> records) {
        super(commands, records);
    }

    public static <T extends BaseModel> Many2Many<T> ofCommands(List<Command<T>> commands) {
        return new Many2Many<>(commands, null);
    }

    public static <T extends BaseModel> Many2Many<T> ofRecords(List<T> records) {
        return new Many2Many<>(null, records);
    }
}
