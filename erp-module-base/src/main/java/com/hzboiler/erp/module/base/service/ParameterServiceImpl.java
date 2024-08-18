package com.hzboiler.erp.module.base.service;

import com.hzboiler.erp.core.method.annotations.Public;
import com.hzboiler.erp.core.service.AbstractBaseService;
import com.hzboiler.erp.module.base.model.Parameter;
import org.springframework.stereotype.Service;

@Service
public class ParameterServiceImpl extends AbstractBaseService<Parameter> implements ParameterService {

    @Public
    @Override
    public Parameter getByKey(String key) {
        return lambdaQuery().eq(Parameter::getKey, key).one();
    }
}
