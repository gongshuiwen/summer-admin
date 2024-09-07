package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.util.SpringContextUtil;
import org.apache.ibatis.session.SqlSession;

/**
 * @author gongshuiwen
 */
public class SpringContextSqlSessionSupplier implements SqlSessionSupplier {

    @Override
    public SqlSession get() {
        return SpringContextUtil.getBean(SqlSession.class);
    }
}