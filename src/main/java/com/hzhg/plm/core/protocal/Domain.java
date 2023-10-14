package com.hzhg.plm.core.protocal;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Domain {

    public static Map<String, SqlKeyword> map = new HashMap<>();
    static {
        map.put("=", SqlKeyword.EQ);
        map.put("!=", SqlKeyword.NE);
        map.put(">", SqlKeyword.GT);
        map.put(">=", SqlKeyword.GE);
        map.put("<", SqlKeyword.LT);
        map.put("<=", SqlKeyword.LE);
    }

    String column;
    String operator;
    Object value;

    public SqlKeyword getSqlKeyword() {
        if (operator == null) {
            throw new IllegalArgumentException("");
        }
        return map.get(operator);
    }
}