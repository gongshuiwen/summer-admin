package com.hzboiler.erp.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hzboiler.erp.core.model.Mock1;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import static com.hzboiler.erp.core.mapper.BaseMapperRegistry.buildMapperInterface;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestBaseMapperRegistry {

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
