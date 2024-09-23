package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.context.BaseContext;

import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * @author gongshuiwen
 */
public interface BaseContextSupplier extends Supplier<BaseContext> {

    BaseContextSupplier DEFAULT = getDefault();

    static BaseContext getBaseContext() {
        return DEFAULT.get();
    }

    private static BaseContextSupplier getDefault() {

        // use service loader
        ServiceLoader<BaseContextSupplier> serviceLoader = ServiceLoader.load(BaseContextSupplier.class);

        // assume only one provider
        for (BaseContextSupplier provider : serviceLoader) {
            return provider;
        }

        throw new RuntimeException();
    }
}
