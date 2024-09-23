package io.summernova.admin.core.context.supplier;

import org.apache.ibatis.session.SqlSessionFactory;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface SqlSessionFactorySupplier extends Supplier<SqlSessionFactory> {

    static SqlSessionFactory getSqlSessionFactory() {
        return new SpringContextSqlSessionFactorySupplier().get();
    }
}
