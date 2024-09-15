package io.summernova.admin.core.mapper;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

/**
 * @author gongshuiwen
 */
final class SqlSessionUtil {

    private static final SqlSessionFactory sqlSessionFactory;
    private static final SqlSession sqlSession;

    static {
        final DataSource dataSource = new HikariDataSource(new HikariConfig("/datasource.properties"));
        final TransactionFactory transactionFactory = new JdbcTransactionFactory();
        final Environment environment = new Environment("development", transactionFactory, dataSource);
        final Configuration configuration = new Configuration(environment);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        sqlSession = sqlSessionFactory.openSession();
    }

    private SqlSessionUtil() {
    }

    static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    static SqlSession getSqlSession() {
        return sqlSession;
    }
}
