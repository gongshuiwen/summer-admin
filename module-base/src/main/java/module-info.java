module summernova.admin.module.base {
    // summernova
    requires summernova.admin.core;
    requires summernova.admin.spring;

    requires com.fasterxml.jackson.annotation;

    requires org.mybatis;
    requires com.baomidou.mybatis.plus.annotation;
    requires com.baomidou.mybatis.plus.core;

    requires spring.beans;
    requires spring.context;
    requires spring.tx;

    requires spring.security.core;
    requires spring.security.crypto;

    requires io.swagger.v3.oas.annotations;

    exports io.summernova.admin.module.base.model;
    exports io.summernova.admin.module.base.security;
    exports io.summernova.admin.module.base.service;

    opens io.summernova.admin.module.base.model to com.baomidou.mybatis.plus.core, summernova.admin.core;
    opens io.summernova.admin.module.base.service to spring.core;
    opens io.summernova.admin.module.base.sql.ddl;
}