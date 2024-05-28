package com.hzboiler.erp.core.mapper;

import com.hzboiler.erp.core.mapper.MapperRelation;
import com.hzboiler.erp.core.mapper.RelationMapper;
import com.hzboiler.erp.core.model.Mock1;
import com.hzboiler.erp.core.model.Mock3;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@MapperRelation(table = "mock_relation",
        field1 = "mock1_id", class1 = Mock1.class,
        field2 = "mock3_id", class2 = Mock3.class)
public interface MockRelationMapper extends RelationMapper {
}
