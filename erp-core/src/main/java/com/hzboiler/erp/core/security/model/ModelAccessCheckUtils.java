package com.hzboiler.erp.core.security.model;

import com.hzboiler.erp.core.context.BaseContext;
import com.hzboiler.erp.core.context.BaseContextHolder;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.security.SimpleGrantedAuthorityPool;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

/**
 * Static utility class for checking model access authority.
 *
 * @author gongshuiwen
 */
public final class ModelAccessCheckUtils {

    // prevent external instantiation
    private ModelAccessCheckUtils() {
    }

    /**
     * Check if the current user has the authority to access the specified model class with the 'SELECT' modelAccessType.
     *
     * @param modelClass The model class which extends {@link BaseModel} to check access for.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    public static void checkSelect(Class<? extends BaseModel> modelClass) throws ModelAccessException {
        check(modelClass, ModelAccessType.SELECT);
    }

    /**
     * Check if the current user has the authority to access the specified model class with the 'CREATE' modelAccessType.
     *
     * @param modelClass The model class which extends {@link BaseModel} to check access for.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    public static void checkCreate(Class<? extends BaseModel> modelClass) throws ModelAccessException {
        check(modelClass, ModelAccessType.CREATE);
    }

    /**
     * Check if the current user has the authority to access the specified model class with the 'UPDATE' modelAccessType.
     *
     * @param modelClass The model class which extends {@link BaseModel} to check access for.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    public static void checkUpdate(Class<? extends BaseModel> modelClass) throws ModelAccessException {
        check(modelClass, ModelAccessType.UPDATE);
    }

    /**
     * Check if the current user has the authority to access the specified model class with the 'DELETE' modelAccessType.
     *
     * @param modelClass The model class which extends {@link BaseModel} to check access for.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    public static void checkDelete(Class<? extends BaseModel> modelClass) throws ModelAccessException {
        check(modelClass, ModelAccessType.DELETE);
    }

    /**
     * Check if the current user has the authority to access the specified model class with the given modelAccessType.
     *
     * @param modelClass      The model class which extends {@link BaseModel} to check access for.
     * @param modelAccessType The {@link ModelAccessType} enumeration to check.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    public static void check(Class<? extends BaseModel> modelClass, ModelAccessType modelAccessType) throws ModelAccessException {
        Objects.requireNonNull(modelClass, "modelClass cannot be null");

        // Get BaseContext
        BaseContext baseContext = BaseContextHolder.getContext();

        // SYS_ADMIN has access to everything
        if (baseContext.isAdmin())
            return;

        // Otherwise, check if the user has the required authority
        GrantedAuthority authorityRequired = getAuthority(modelClass, modelAccessType);
        if (baseContext.getAuthorities().contains(authorityRequired))
            return;

        throw new ModelAccessException(modelClass, modelAccessType);
    }

    private static GrantedAuthority getAuthority(Class<? extends BaseModel> modelClass, ModelAccessType authority) {
        return SimpleGrantedAuthorityPool.of(authority.getPrefix() + modelClass.getSimpleName());
    }
}
