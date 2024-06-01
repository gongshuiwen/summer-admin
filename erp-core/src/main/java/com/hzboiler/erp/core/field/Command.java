package com.hzboiler.erp.core.field;

import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

import java.util.List;

/**
 * @author gongshuiwen
 */
@Getter
public class Command<T extends BaseModel> {

    private CommandType commandType;
    private List<Long> ids;
    private List<T> records;

    // ===================================================
    // Static factory methods for Many2Many field commands
    // ===================================================
    public static <T extends BaseModel> Command<T> add(List<Long> ids) {
        Command<T> command = new Command<>();
        command.commandType = CommandType.ADD;
        command.ids = ids;
        return command;
    }

    public static <T extends BaseModel> Command<T> remove(List<Long> ids) {
        Command<T> command = new Command<>();
        command.commandType = CommandType.REMOVE;
        command.ids = ids;
        return command;
    }

    public static <T extends BaseModel> Command<T> replace(List<Long> ids) {
        Command<T> command = new Command<>();
        command.commandType = CommandType.REPLACE;
        command.ids = ids;
        return command;
    }

    // ==================================================
    // Static factory methods for One2Many field commands
    // ==================================================
    public static <T extends BaseModel> Command<T> create(List<T> records) {
        Command<T> command = new Command<>();
        command.commandType = CommandType.CREATE;
        command.records = records;
        return command;
    }

    public static <T extends BaseModel> Command<T> delete(List<Long> ids) {
        Command<T> command = new Command<>();
        command.commandType = CommandType.DELETE;
        command.ids = ids;
        return command;
    }

    public static <T extends BaseModel> Command<T> update(Long id, T record) {
        Command<T> command = new Command<>();
        command.commandType = CommandType.UPDATE;
        command.ids = List.of(id);
        command.records = List.of(record);
        return command;
    }
}
