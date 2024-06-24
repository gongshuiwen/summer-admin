package com.hzboiler.erp.test.annotation;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "admin", roles = "SYS_ADMIN")
public @interface WithMockAdmin {}