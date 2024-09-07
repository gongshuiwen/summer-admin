module summernova.admin.test {
    requires summernova.admin.common;
    requires spring.boot.autoconfigure;
    requires spring.security.test;

    exports io.summernova.admin;
    exports io.summernova.admin.test.annotation;
}