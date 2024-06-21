package com.hzboiler.erp.core.security.model;

import com.hzboiler.erp.core.exception.BusinessException;
import com.hzboiler.erp.core.exception.CoreBusinessExceptionEnums;
import com.hzboiler.erp.core.model.BaseModel;

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
