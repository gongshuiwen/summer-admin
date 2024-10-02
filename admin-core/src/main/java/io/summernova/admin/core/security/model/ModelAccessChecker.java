package io.summernova.admin.core.security.model;

import io.summernova.admin.core.domain.model.BaseModel;

/**
 * @author gongshuiwen
 */
public interface ModelAccessChecker {

    /**
     * Check if the current user has the authority to access the specified model class with the 'SELECT' modelAccessType.
     *
     * @param modelClass The model class which extends {@link BaseModel} to check access for.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    default void checkModelSelect(Class<? extends BaseModel> modelClass) throws ModelAccessException {
        checkModelAccess(modelClass, ModelAccessType.SELECT);
    }

    /**
     * Check if the current user has the authority to access the specified model class with the 'CREATE' modelAccessType.
     *
     * @param modelClass The model class which extends {@link BaseModel} to check access for.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    default void checkModelCreate(Class<? extends BaseModel> modelClass) throws ModelAccessException {
        checkModelAccess(modelClass, ModelAccessType.CREATE);
    }

    /**
     * Check if the current user has the authority to access the specified model class with the 'UPDATE' modelAccessType.
     *
     * @param modelClass The model class which extends {@link BaseModel} to check access for.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    default void checkModelUpdate(Class<? extends BaseModel> modelClass) throws ModelAccessException {
        checkModelAccess(modelClass, ModelAccessType.UPDATE);
    }

    /**
     * Check if the current user has the authority to access the specified model class with the 'DELETE' modelAccessType.
     *
     * @param modelClass The model class which extends {@link BaseModel} to check access for.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    default void checkModelDelete(Class<? extends BaseModel> modelClass) throws ModelAccessException {
        checkModelAccess(modelClass, ModelAccessType.DELETE);
    }

    /**
     * Check if the current user has the authority to access the specified model class with the given modelAccessType.
     *
     * @param modelClass      The model class which extends {@link BaseModel} to check access for.
     * @param modelAccessType The {@link ModelAccessType} enumeration to check.
     * @throws ModelAccessException If the user does not have the required authority.
     */
    void checkModelAccess(Class<? extends BaseModel> modelClass, ModelAccessType modelAccessType);
}
