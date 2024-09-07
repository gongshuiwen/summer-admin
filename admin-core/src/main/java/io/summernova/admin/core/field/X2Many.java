package io.summernova.admin.core.field;

import io.summernova.admin.core.model.BaseModel;
import lombok.Getter;

import java.util.List;

/**
 * @author gongshuiwen
 */
@Getter
abstract class X2Many<T extends BaseModel> {

    private final List<Command<T>> commands; // for update use
    private final List<T> records;

    protected X2Many(List<Command<T>> commands, List<T> records) {
        this.commands = commands;
        this.records = records;
    }
}
