package com.hzboiler.erp.core.annotaion;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.hzboiler.erp.core.security.Constants.CODE_SYS_ADMIN;


@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = CODE_SYS_ADMIN)
public @interface WithMockAdmin {}