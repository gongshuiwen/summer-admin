package io.summernova.admin.test.context;

import io.summernova.admin.core.context.BaseContext;
import io.summernova.admin.core.context.supplier.BaseContextSupplier;

/**
 * @author gongshuiwen
 */
public final class CustomBaseContextSupplier implements BaseContextSupplier {

    private static BaseContext baseContext;

    public static void setBaseContext(BaseContext baseContext) {
        CustomBaseContextSupplier.baseContext = baseContext;
    }

    @Override
    public BaseContext get() {
        return baseContext;
    }
}
