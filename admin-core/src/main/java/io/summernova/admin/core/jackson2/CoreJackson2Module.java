package io.summernova.admin.core.jackson2;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.summernova.admin.core.domain.field.CommandType;
import io.summernova.admin.core.domain.model.BaseModel;

/**
 * @author gongshuiwen
 */
public class CoreJackson2Module extends SimpleModule {

    public CoreJackson2Module() {
        super(CoreJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(BaseModel.class, BaseModelMixin.class);
        context.setMixInAnnotations(CommandType.class, CommonTypeMixin.class);
    }
}
