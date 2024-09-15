package io.summernova.admin.core.mapper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.summernova.admin.core.model.Mock1;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author gongshuiwen
 */
class TestBaseMapperRegistry {

    static final SqlSessionFactory sqlSessionFactory;
    static final SqlSession sqlSession;

    static {
        final DataSource dataSource = new HikariDataSource(new HikariConfig("/datasource.properties"));
        final TransactionFactory transactionFactory = new JdbcTransactionFactory();
        final Environment environment = new Environment("development", transactionFactory, dataSource);
        final Configuration configuration = new Configuration(environment);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        sqlSession = sqlSessionFactory.openSession();
    }

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
