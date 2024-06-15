package com.hzboiler.erp.core.service;

import com.hzboiler.erp.core.model.BaseUser;

/**
 * @author gongshuiwen
 */
public interface BaseUserService {

    /**
     * Get {@link BaseUser} by user id.
     * @param userId id of user
     * @return {@link BaseUser}
     */
    BaseUser selectById(Long userId);
}
