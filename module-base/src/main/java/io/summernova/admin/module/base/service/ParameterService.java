package io.summernova.admin.module.base.service;

import io.summernova.admin.core.service.BaseService;
import io.summernova.admin.module.base.model.Parameter;

public interface ParameterService extends BaseService<Parameter> {

    Parameter getByKey(String key);
}
