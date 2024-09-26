module summernova.admin.core {
    // uses
    uses io.summernova.admin.core.context.supplier.BaseContextSupplier;
    uses io.summernova.admin.core.context.supplier.BaseAuthoritiesServiceSupplier;
    uses io.summernova.admin.core.context.supplier.BaseUserServiceSupplier;
    uses io.summernova.admin.core.context.supplier.HttpServletRequestSupplier;
    uses io.summernova.admin.core.context.supplier.SqlSessionFactorySupplier;
    uses io.summernova.admin.core.context.supplier.SqlSessionSupplier;

    // summernova
    requires transitive summernova.admin.common;

    // jackson
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    // mybatis && mybatis-plus
    requires org.mybatis;
    requires com.baomidou.mybatis.plus.annotation;
    requires com.baomidou.mybatis.plus.core;
    requires com.baomidou.mybatis.plus.extension;

    // spring
    requires spring.tx;

    // other
    requires transitive org.slf4j;
    requires net.bytebuddy;
    requires net.bytebuddy.agent;

    // automatic
    requires io.swagger.v3.oas.annotations;

    // exports
    exports io.summernova.admin.core.context;
    exports io.summernova.admin.core.context.supplier;
    exports io.summernova.admin.core.dal.mapper;
    exports io.summernova.admin.core.domain.field;
    exports io.summernova.admin.core.domain.field.annotations;
    exports io.summernova.admin.core.domain.field.util;
    exports io.summernova.admin.core.domain.model;
    exports io.summernova.admin.core.jackson2;
    exports io.summernova.admin.core.mybatis;
    exports io.summernova.admin.core.security;
    exports io.summernova.admin.core.security.account;
    exports io.summernova.admin.core.security.authorization;
    exports io.summernova.admin.core.security.model;
    exports io.summernova.admin.core.service;
    exports io.summernova.admin.core.service.annotations;

    // opens
    opens io.summernova.admin.core.dal.mapper to org.mybatis, com.baomidou.mybatis.plus.core, spring.core;
    opens io.summernova.admin.core.domain.field to com.fasterxml.jackson.databind;
    opens io.summernova.admin.core.domain.model;
    opens io.summernova.admin.core.jackson2 to com.fasterxml.jackson.databind;
    opens io.summernova.admin.core.service to spring.core;
}