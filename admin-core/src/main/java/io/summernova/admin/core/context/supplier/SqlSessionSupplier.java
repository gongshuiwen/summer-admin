package io.summernova.admin.core.context.supplier;

import org.apache.ibatis.session.SqlSession;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface SqlSessionSupplier extends Supplier<SqlSession> {

    static SqlSession getSqlSession() {
        return new SpringContextSqlSessionSupplier().get();
    }
}
