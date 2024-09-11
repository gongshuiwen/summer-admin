module summernova.admin.common {
    requires transitive jakarta.servlet;
    requires transitive jakarta.validation;
    requires static transitive lombok;

    exports io.summernova.admin.common.exception;
    exports io.summernova.admin.common.protocal;
    exports io.summernova.admin.common.util;
    exports io.summernova.admin.common.validation;

    opens io.summernova.admin.common.validation to spring.core;
}
