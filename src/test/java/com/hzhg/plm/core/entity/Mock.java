package com.hzhg.plm.core.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Mock extends BaseEntity {

    private String name;

    public Mock(String name) {
        this.name = name;
    }

    @Override
    public String getDisplayName() {
        return getName();
    }
}
