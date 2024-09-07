package io.summernova.admin.core.security.model;

import io.summernova.admin.core.exception.BusinessException;
import io.summernova.admin.core.exception.CoreBusinessExceptionEnums;
import io.summernova.admin.core.model.BaseModel;

/**
 * Model access exception
 *
 * @author gongshuiwen
 */
public final class ModelAccessException extends BusinessException {
    public ModelAccessException(Class<? extends BaseModel> modelClass, ModelAccessType authority) {
        super(CoreBusinessExceptionEnums.ERROR_ACCESS_DENIED,
                "Model access denied: [" + authority.name() + "] " + modelClass.getSimpleName());
    }
}
