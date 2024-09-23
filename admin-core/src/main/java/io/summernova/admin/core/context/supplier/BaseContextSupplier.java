package io.summernova.admin.core.context.supplier;

import io.summernova.admin.core.context.BaseContext;

import java.lang.reflect.InvocationTargetException;
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
        // use CustomBaseContextSupplier priorly when in test environment
        try {
            Class<?> baseContextSupplier = BaseContextSupplier.class.getClassLoader()
                    .loadClass("io.summernova.admin.core.context.CustomBaseContextSupplier");
            if (baseContextSupplier != null) {
                return (BaseContextSupplier) baseContextSupplier.getConstructor().newInstance();
            }
        } catch (ClassNotFoundException e) {
            // ignore
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        return ServiceLoaderUtil.getProvider(BaseContextSupplier.class);
    }
}
