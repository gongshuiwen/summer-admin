package io.summernova.admin.core.dal.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.domain.model.Mock1;
import io.summernova.admin.test.dal.ScriptRunnerUtil;
import io.summernova.admin.test.dal.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestBaseMapperRegistry {

    static final SqlSession sqlSession = SqlSessionUtil.getSqlSession();

    @Test
    void testGetBaseMapper() {
        ScriptRunnerUtil.runScript(sqlSession, "mock1.sql");
        BaseMapper<Mock1> baseMapper = BaseMapperRegistry.getBaseMapper(sqlSession, Mock1.class);
        assertNotNull(baseMapper);

        List<Mock1> mock1s = baseMapper.selectList(new QueryWrapper<>());
        assertEquals(2, mock1s.size());

        sqlSession.rollback();
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
