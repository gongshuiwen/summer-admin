module summernova.admin.common {
    requires transitive java.sql;
    requires transitive jakarta.servlet;
    requires transitive jakarta.validation;
    requires transitive org.slf4j;
    requires static transitive lombok;

    exports io.summernova.admin.common.exception;
    exports io.summernova.admin.common.protocal;
    exports io.summernova.admin.common.query;
    exports io.summernova.admin.common.util;
    exports io.summernova.admin.common.validation;

    opens io.summernova.admin.common.validation to spring.core;
}
