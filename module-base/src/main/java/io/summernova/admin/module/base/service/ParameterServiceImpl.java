package io.summernova.admin.module.base.service;

import io.summernova.admin.core.method.annotations.Public;
import io.summernova.admin.core.service.AbstractBaseService;
import io.summernova.admin.module.base.model.Parameter;
import org.springframework.stereotype.Service;

@Service
public class ParameterServiceImpl extends AbstractBaseService<Parameter> implements ParameterService {

    @Public
    @Override
    public Parameter getByKey(String key) {
        return lambdaQuery().eq(Parameter::getKey, key).one();
    }
}
