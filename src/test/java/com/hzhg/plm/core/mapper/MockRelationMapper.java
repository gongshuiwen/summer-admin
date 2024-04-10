package com.hzhg.plm.core.mapper;

import com.hzhg.plm.core.entity.Mock1;
import com.hzhg.plm.core.entity.Mock3;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@MapperRelation(table = "mock_relation",
        field1 = "mock1_id", class1 = Mock1.class,
        field2 = "mock3_id", class2 = Mock3.class)
public interface MockRelationMapper extends RelationMapper {
}
