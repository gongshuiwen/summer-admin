package io.summernova.admin.core.mapper;

import io.summernova.admin.core.model.Mock1;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestBaseMapperRegistry {

    static final SqlSession sqlSession = SqlSessionUtil.getSqlSession();

    @Test
    void testGetBaseMapper() {
        BaseMapper<Mock1> mapper = BaseMapperRegistry.getBaseMapper(sqlSession, Mock1.class);
        assertNotNull(mapper);
    }

    @Test
    void testBuildMapperInterface() throws ClassNotFoundException {
        Class<?> modelClass = Mock1.class;
        Class<?> mapperInterface;
        mapperInterface = BaseMapperRegistry.buildMapperInterface(modelClass);

        assertNotNull(mapperInterface);
        assertTrue(Modifier.isPublic(mapperInterface.getModifiers()));
        assertTrue(Modifier.isInterface(mapperInterface.getModifiers()));
        assertEquals(1, mapperInterface.getGenericInterfaces().length);

        ParameterizedType parameterizedType = (ParameterizedType) mapperInterface.getGenericInterfaces()[0];
        assertEquals(BaseMapper.class, parameterizedType.getRawType());
        assertEquals(1, parameterizedType.getActualTypeArguments().length);
        assertEquals(modelClass, parameterizedType.getActualTypeArguments()[0]);

        assertEquals(mapperInterface, Class.forName(mapperInterface.getName()));
    }
}
