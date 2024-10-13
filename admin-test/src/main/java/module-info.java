module summernova.admin.test {
    requires summernova.admin.common;
    requires summernova.admin.core;

    requires spring.boot.autoconfigure;
    requires spring.security.test;

    exports io.summernova.admin;
    exports io.summernova.admin.test.annotation;
}