package io.summernova.admin.core.context;

import io.summernova.admin.core.dal.mapper.SqlSessionUtil;
import io.summernova.admin.core.security.account.BaseUser;
import io.summernova.admin.core.security.authorization.BaseAuthority;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Set;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
public final class MockBaseContextImpl extends BaseContextImpl {

    private BaseUser user;
    private Set<? extends BaseAuthority> authorities;
    private SqlSession sqlSession;

    public MockBaseContextImpl(Long userId) {
        super(userId);
    }

    @Override
    public SqlSessionFactory getSqlSessionFactory() {
        return SqlSessionUtil.getSqlSessionFactory();
    }

    @Override
    public SqlSession getSqlSession() {
        if (sqlSession == null) {
            sqlSession = SqlSessionUtil.getSqlSessionFactory().openSession();
        }
        return sqlSession;
    }
}
