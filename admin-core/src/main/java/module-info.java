module summernova.admin.core {
    // summernova
    requires transitive summernova.admin.common;

    // java
    requires java.sql;

    // jakarta
    requires jakarta.servlet;
    requires jakarta.validation;

    // jackson
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

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
    requires spring.webmvc;

    // spring data
    requires spring.data.redis;

    // spring security
    requires spring.security.config;
    requires spring.security.core;
    requires spring.security.crypto;
    requires spring.security.web;

    // other
    requires static transitive lombok;
    requires transitive org.slf4j;
    requires io.swagger.v3.oas.annotations;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;

    // exports
    exports io.summernova.admin.core.config;
    exports io.summernova.admin.core.context;
    exports io.summernova.admin.core.context.supplier;
    exports io.summernova.admin.core.controller;
    exports io.summernova.admin.core.exception;
    exports io.summernova.admin.core.field;
    exports io.summernova.admin.core.field.annotations;
    exports io.summernova.admin.core.jackson2;
    exports io.summernova.admin.core.mapper;
    exports io.summernova.admin.core.method.annotations;
    exports io.summernova.admin.core.model;
    exports io.summernova.admin.core.protocal;
    exports io.summernova.admin.core.protocal.query;
    exports io.summernova.admin.core.security;
    exports io.summernova.admin.core.security.account;
    exports io.summernova.admin.core.security.authorization;
    exports io.summernova.admin.core.security.model;
    exports io.summernova.admin.core.service;
    exports io.summernova.admin.core.web;

    // opens
    opens io.summernova.admin.core.config to spring.core;
    opens io.summernova.admin.core.controller;
    opens io.summernova.admin.core.field to com.fasterxml.jackson.databind;
    opens io.summernova.admin.core.jackson2 to com.fasterxml.jackson.databind;
    opens io.summernova.admin.core.mapper to com.baomidou.mybatis.plus.core, spring.core;
    opens io.summernova.admin.core.model to com.baomidou.mybatis.plus.core;
    opens io.summernova.admin.core.service to spring.core;
}