module summernova.admin.test {
    // summernova
    requires summernova.admin.common;
    requires summernova.admin.core;

    requires org.junit.jupiter.api;
    requires org.mybatis;
    requires com.baomidou.mybatis.plus.extension;
    requires com.baomidou.mybatis.plus.annotation;
    requires com.baomidou.mybatis.plus.core;
    requires com.zaxxer.hikari;

    exports io.summernova.admin.test.annotation;
    exports io.summernova.admin.test.context;
    exports io.summernova.admin.test.dal;
}