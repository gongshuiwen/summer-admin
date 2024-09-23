package io.summernova.admin.core.context.supplier;

import java.util.ServiceLoader;

/**
 * @author gongshuiwen
 */
final class ServiceLoaderUtil {

    // prevent external instantiation
    private ServiceLoaderUtil() {
    }

    static <T> T getProvider(Class<T> clazz) {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);

        // assume only one provider
        for (T provider : serviceLoader) {
            return provider;
        }

        throw new RuntimeException();
    }
}
