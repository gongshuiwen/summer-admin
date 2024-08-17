package com.hzboiler.erp.core.context.supplier;

import com.hzboiler.erp.core.util.SpringContextUtil;
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
