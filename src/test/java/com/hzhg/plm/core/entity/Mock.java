package com.hzhg.plm.core.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mock extends BaseEntity {

    private String name;

    @Override
    public String getDisplayName() {
        return getName();
    }
}
