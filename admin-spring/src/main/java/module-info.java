module summernova.admin.spring {
    requires summernova.admin.core;

    // mybatis
    requires org.mybatis;
    requires com.baomidou.mybatis.plus.annotation;
    requires com.baomidou.mybatis.plus.core;
    requires com.baomidou.mybatis.plus.extension;
    requires com.baomidou.mybatis.plus.spring.boot.autoconfigure;

    // jackson
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    // spring
    requires spring.aop;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.tx;
    requires spring.web;
    requires spring.webmvc;

    // spring data
    requires spring.data.redis;

    // spring security
    requires spring.security.config;
    requires spring.security.core;
    requires spring.security.crypto;
    requires spring.security.web;
    requires io.swagger.v3.oas.annotations;
    requires spring.boot.autoconfigure;

    // exports
    exports io.summernova.admin.spring.config to spring.beans, spring.context;
    exports io.summernova.admin.spring.security.web to spring.beans, spring.context;
    exports io.summernova.admin.spring.security to spring.beans, spring.context;
    exports io.summernova.admin.spring.web to com.fasterxml.jackson.databind, org.mybatis, spring.beans, spring.context, spring.web;

    // opens
    opens io.summernova.admin.spring.config to spring.core;
    opens io.summernova.admin.spring.security.web to spring.core;
    opens io.summernova.admin.spring.web to spring.core, com.baomidou.mybatis.plus.core, summernova.admin.core;

    // provides
    provides io.summernova.admin.core.context.supplier.BaseContextSupplier with
            io.summernova.admin.spring.context.supplier.SpringSecurityBaseContextSupplier;
    provides io.summernova.admin.core.context.supplier.BaseAuthoritiesServiceSupplier with
            io.summernova.admin.spring.context.supplier.SpringContextBaseAuthoritiesServiceSupplier;
    provides io.summernova.admin.core.context.supplier.BaseUserServiceSupplier with
            io.summernova.admin.spring.context.supplier.SpringContextBaseUserServiceSupplier;
    provides io.summernova.admin.core.context.supplier.HttpServletRequestSupplier with
            io.summernova.admin.spring.context.supplier.SpringWebHttpServletRequestSupplier;
    provides io.summernova.admin.core.context.supplier.SqlSessionSupplier with
            io.summernova.admin.spring.context.supplier.SpringContextSqlSessionSupplier;
    provides io.summernova.admin.core.context.supplier.SqlSessionFactorySupplier with
            io.summernova.admin.spring.context.supplier.SpringContextSqlSessionFactorySupplier;
}