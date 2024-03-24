package com.hzhg.plm.core.annotaion;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.hzhg.plm.core.controller.BaseController.ROLE_ADMIN;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = ROLE_ADMIN)
public @interface WithMockAdmin {}