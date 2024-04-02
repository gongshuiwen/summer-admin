package com.hzhg.plm.core.fields;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hzhg.plm.core.entity.BaseEntity;
import com.hzhg.plm.core.jackson2.Many2ManyDeserializer;
import lombok.Getter;

import java.util.List;

@Getter
@JsonDeserialize(using = Many2ManyDeserializer.class)
public class Many2Many<T extends BaseEntity> {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Command<T>> commands; // for update use
    private List<T> values;

    public static <T extends BaseEntity> Many2Many<T> ofCommands(List<Command<T>> commands) {
        Many2Many<T> many2Many = new Many2Many<>();
        many2Many.commands = commands;
        return many2Many;
    }

    public static <T extends BaseEntity> Many2Many<T> ofValues(List<T> values) {
        Many2Many<T> many2Many = new Many2Many<>();
        many2Many.values = values;
        return many2Many;
    }

    List<T> get() {
        return values;
    }
}
