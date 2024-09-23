package io.summernova.admin.core.context;

import io.summernova.admin.core.security.account.BaseUser;
import io.summernova.admin.core.security.authorization.BaseAuthority;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author gongshuiwen
 */
@Getter
@Setter
public final class MockBaseContextImpl extends BaseContextImpl {

    private BaseUser user;
    private Set<? extends BaseAuthority> authorities;

    public MockBaseContextImpl(Long userId) {
        super(userId);
    }
}
