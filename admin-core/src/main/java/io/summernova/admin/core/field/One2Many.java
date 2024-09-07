package io.summernova.admin.core.field;

import io.summernova.admin.core.model.BaseModel;

import java.util.List;

/**
 * @author gongshuiwen
 */
public final class One2Many<T extends BaseModel> extends X2Many<T> {

    // prevent external instantiation, just for deserialization
    private One2Many() {
        super(null, null);
    }

    // prevent external instantiation
    private One2Many(List<Command<T>> commands, List<T> records) {
        super(commands, records);
    }

    public static <T extends BaseModel> One2Many<T> ofCommands(List<Command<T>> commands) {
        return new One2Many<>(commands, null);
    }

    public static <T extends BaseModel> One2Many<T> ofRecords(List<T> records) {
        return new One2Many<>(null, records);
    }
}
