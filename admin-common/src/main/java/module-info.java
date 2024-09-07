module summernova.admin.common {
    requires transitive jakarta.servlet;
    requires transitive jakarta.validation;

    exports io.summernova.admin.common.util;
    exports io.summernova.admin.common.validation;

    opens io.summernova.admin.common.validation to spring.core;
}
