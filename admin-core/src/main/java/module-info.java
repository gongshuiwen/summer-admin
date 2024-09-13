module summernova.admin.core {
    // summernova
    requires transitive summernova.admin.common;

    // jackson
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    // mybatis && mybatis-plus
    requires org.mybatis;
    requires org.mybatis.spring;
    requires com.baomidou.mybatis.plus.annotation;
    requires com.baomidou.mybatis.plus.core;
    requires com.baomidou.mybatis.plus.extension;
    requires com.baomidou.mybatis.plus.spring.boot.autoconfigure;

    // spring
    requires spring.aop;
    requires spring.beans;
    requires spring.context;
    requires spring.core;
    requires spring.tx;
    requires spring.web;

    // spring security
    requires spring.security.core;

    // other
    requires transitive org.slf4j;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;

    // automatic
    requires io.swagger.v3.oas.annotations;

    // exports
    exports io.summernova.admin.core.context;
    exports io.summernova.admin.core.context.supplier;
    exports io.summernova.admin.core.field;
    exports io.summernova.admin.core.field.annotations;
    exports io.summernova.admin.core.field.util;
    exports io.summernova.admin.core.jackson2;
    exports io.summernova.admin.core.mapper;
    exports io.summernova.admin.core.method.annotations;
    exports io.summernova.admin.core.method.util;
    exports io.summernova.admin.core.model;
    exports io.summernova.admin.core.mybatis;
    exports io.summernova.admin.core.protocal.query;
    exports io.summernova.admin.core.security;
    exports io.summernova.admin.core.security.account;
    exports io.summernova.admin.core.security.authorization;
    exports io.summernova.admin.core.security.model;
    exports io.summernova.admin.core.service;
    exports io.summernova.admin.core.util;

    // opens
    opens io.summernova.admin.core.config;
    opens io.summernova.admin.core.field to com.fasterxml.jackson.databind;
    opens io.summernova.admin.core.jackson2 to com.fasterxml.jackson.databind;
    opens io.summernova.admin.core.mapper to com.baomidou.mybatis.plus.core, spring.core;
    opens io.summernova.admin.core.model;
    opens io.summernova.admin.core.service to spring.core;
}