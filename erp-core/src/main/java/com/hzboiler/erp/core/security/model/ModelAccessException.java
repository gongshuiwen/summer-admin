package com.hzboiler.erp.core.security.model;

import com.hzboiler.erp.core.model.BaseModel;

/**
 * @author gongshuiwen
 */
public final class ModelAccessException extends RuntimeException {
    public ModelAccessException(Class<? extends BaseModel> modelClass, ModelAccessType authority) {
        super("Model access denied: [" + authority.name() + "] " + modelClass.getSimpleName());
    }
}
