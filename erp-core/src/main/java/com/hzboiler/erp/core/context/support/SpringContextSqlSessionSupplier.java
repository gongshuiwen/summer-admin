package com.hzboiler.erp.core.context.support;

import com.hzboiler.erp.core.util.SpringContextUtil;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;

/**
 * @author gongshuiwen
 */
public class SpringContextSqlSessionSupplier implements SqlSessionSupplier {

    @Override
    public SqlSession get() {
        return SpringContextUtil.getBean(SqlSessionTemplate.class);
    }
}
