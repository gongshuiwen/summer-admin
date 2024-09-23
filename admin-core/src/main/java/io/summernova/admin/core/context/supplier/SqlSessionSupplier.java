package io.summernova.admin.core.context.supplier;

import org.apache.ibatis.session.SqlSession;

import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface SqlSessionSupplier extends Supplier<SqlSession> {

    SqlSessionSupplier DEFAULT = ServiceLoaderUtil.getProvider(SqlSessionSupplier.class);

    static SqlSession getSqlSession() {
        return DEFAULT.get();
    }
}
