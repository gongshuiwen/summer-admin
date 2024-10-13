module summernova.admin.test {
    requires summernova.admin.common;
    requires summernova.admin.core;

    requires spring.boot.autoconfigure;
    requires spring.security.test;
    requires org.junit.jupiter.api;
    requires com.zaxxer.hikari;
    requires org.mybatis;
    requires com.baomidou.mybatis.plus.extension;
    requires com.baomidou.mybatis.plus.annotation;
    requires com.baomidou.mybatis.plus.core;

    exports io.summernova.admin;
    exports io.summernova.admin.test.annotation;
}