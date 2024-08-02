package com.hzboiler.erp.core.mapper;

import com.hzboiler.erp.core.model.Mock1;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import static com.hzboiler.erp.core.mapper.BaseMapperRegistry.buildMapperInterface;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
@SpringBootTest
class TestBaseMapperRegistry {

    @Autowired
    SqlSession sqlSession;

    @Test
    void testGetBaseMapper() {
        BaseMapper<Mock1> mapper = BaseMapperRegistry.getBaseMapper(sqlSession, Mock1.class);
        assertNotNull(mapper);
    }

    @Test
    void testBuildMapperInterface() throws ClassNotFoundException {
        Class<?> modelClass = Mock1.class;
        Class<?> mapperInterface;
        mapperInterface = buildMapperInterface(modelClass);

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
