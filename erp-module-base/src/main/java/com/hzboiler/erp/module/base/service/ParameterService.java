package com.hzboiler.erp.module.base.service;

import com.hzboiler.erp.core.service.BaseService;
import com.hzboiler.erp.module.base.model.Parameter;

public interface ParameterService extends BaseService<Parameter> {

    Parameter getByKey(String key);
}
