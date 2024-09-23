package io.summernova.admin.core.mapper;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.summernova.admin.core.field.Many2One;
import io.summernova.admin.core.mybatis.Many2OneTypeHandler;
import io.summernova.admin.core.mybatis.MyMetaObjectHandler;
import lombok.Getter;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

/**
 * @author gongshuiwen
 */
public final class SqlSessionUtil {

    @Getter
    private static final SqlSessionFactory sqlSessionFactory;

    @Getter
    private static final SqlSession sqlSession;

    static {
        final DataSource dataSource = new HikariDataSource(new HikariConfig("/datasource.properties"));
        final TransactionFactory transactionFactory = new JdbcTransactionFactory();
        final Environment environment = new Environment("test", transactionFactory, dataSource);
        final MybatisConfiguration configuration = new MybatisConfiguration(environment);

        final MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        configuration.addInterceptor(mybatisPlusInterceptor);
        configuration.getTypeHandlerRegistry().register(Many2One.class, new Many2OneTypeHandler());

        GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
        GlobalConfigUtils.setGlobalConfig(configuration, globalConfig);

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        sqlSession = sqlSessionFactory.openSession();
    }

    private SqlSessionUtil() {
    }
}
