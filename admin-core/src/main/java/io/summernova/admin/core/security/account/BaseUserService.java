package io.summernova.admin.core.security.account;

/**
 * @author gongshuiwen
 */
public interface BaseUserService {

    /**
     * Load {@link BaseUser} by user id.
     * @param userId id of user
     * @return {@link BaseUser}
     */
    BaseUser loadUserByUserId(Long userId);
}
