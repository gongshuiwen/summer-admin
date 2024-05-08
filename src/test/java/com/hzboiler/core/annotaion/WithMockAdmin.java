package com.hzboiler.core.annotaion;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.hzboiler.core.utils.Constants.ROLE_SYS_ADMIN;


@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = ROLE_SYS_ADMIN)
public @interface WithMockAdmin {}