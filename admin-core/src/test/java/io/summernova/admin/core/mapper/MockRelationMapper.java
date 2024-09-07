package io.summernova.admin.core.mapper;

import io.summernova.admin.core.model.Mock1;
import io.summernova.admin.core.model.Mock3;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author gongshuiwen
 */
@Mapper
@MapperRelation(table = "mock_relation",
        field1 = "mock1_id", class1 = Mock1.class,
        field2 = "mock3_id", class2 = Mock3.class)
public interface MockRelationMapper extends RelationMapper {
}
