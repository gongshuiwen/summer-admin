package io.summernova.admin.core.annotaion;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static io.summernova.admin.core.security.Constants.CODE_SYS_ADMIN;


@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(roles = CODE_SYS_ADMIN)
public @interface WithMockAdmin {}