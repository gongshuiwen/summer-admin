package io.summernova.admin.spring.context.supplier;

import io.summernova.admin.core.context.supplier.SqlSessionFactorySupplier;
import io.summernova.admin.core.util.SpringContextUtil;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author gongshuiwen
 */
public class SpringContextSqlSessionFactorySupplier implements SqlSessionFactorySupplier {

    @Override
    public SqlSessionFactory get() {
        return SpringContextUtil.getBean(SqlSessionFactory.class);
    }
}
