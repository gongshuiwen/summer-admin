package com.hzboiler.erp.core.field;

import com.hzboiler.erp.core.model.BaseModel;
import lombok.Getter;

import java.util.List;

/**
 * @author gongshuiwen
 */
@Getter
public class One2Many<T extends BaseModel> {

    private List<Command<T>> commands; // for update use
    private List<T> values;

    public static <T extends BaseModel> One2Many<T> ofCommands(List<Command<T>> commands) {
        One2Many<T> One2Many = new One2Many<>();
        One2Many.commands = commands;
        return One2Many;
    }

    public static <T extends BaseModel> One2Many<T> ofValues(List<T> values) {
        One2Many<T> One2Many = new One2Many<>();
        One2Many.values = values;
        return One2Many;
    }

    public List<T> get() {
        return values;
    }
}
