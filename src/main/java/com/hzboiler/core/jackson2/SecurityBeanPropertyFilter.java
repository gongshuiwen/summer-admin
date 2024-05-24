package com.hzboiler.core.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.hzboiler.core.context.BaseContextHolder;
import com.hzboiler.core.security.GrantedAuthorityCheckUtils;
import com.hzboiler.core.entity.BaseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

import static com.hzboiler.core.security.Constants.ROLE_PREFIX;
import static com.hzboiler.core.security.Constants.ROLE_SYS_ADMIN;

/**
 * @author gongshuiwen
 */
public class SecurityBeanPropertyFilter extends SimpleBeanPropertyFilter {

    public static final String FILTER_ID = "securityBeanPropertyFilter";
    public static final SimpleBeanPropertyFilter INSTANCE = new SecurityBeanPropertyFilter();

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        if (checkAllowedForAdminAndRoles(writer) && checkAllowedForCreateAndUpdateUser(pojo, writer)) {
            writer.serializeAsField(pojo, jgen, provider);
        }
    }

    private boolean checkAllowedForAdminAndRoles(PropertyWriter writer) {
        AllowedForAdmin allowedForAdminAnnotation = writer.findAnnotation(AllowedForAdmin.class);
        AllowedForRoles allowedForRolesAnnotation = writer.findAnnotation(AllowedForRoles.class);
        if (allowedForAdminAnnotation == null && allowedForRolesAnnotation == null) {
            return true;
        } else if (allowedForAdminAnnotation == null) {
            return checkAllowedForRoles(allowedForRolesAnnotation);
        }else if (allowedForRolesAnnotation == null) {
            return checkAllowedForAdmin(allowedForAdminAnnotation);
        }  else {
            return checkAllowedForAdmin(allowedForAdminAnnotation) || checkAllowedForRoles(allowedForRolesAnnotation);
        }
    }

    private boolean checkAllowedForAdmin(AllowedForAdmin allowedForAdminAnnotation) {
        if (allowedForAdminAnnotation != null) {
            return GrantedAuthorityCheckUtils.contains(new SimpleGrantedAuthority(ROLE_SYS_ADMIN));
        }
        return true;
    }

    private boolean checkAllowedForRoles(AllowedForRoles allowedForRolesAnnotation) {
        if (allowedForRolesAnnotation != null
                && allowedForRolesAnnotation.value() != null
                && allowedForRolesAnnotation.value().length > 0) {
            return GrantedAuthorityCheckUtils.containsAny(
                    Arrays.stream(allowedForRolesAnnotation.value())
                            .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                            .toList());
        }
        return true;
    }

    private boolean checkAllowedForCreateAndUpdateUser(Object pojo, PropertyWriter writer) {
        AllowedForCreateUser allowedForCreateUserAnnotation = writer.findAnnotation(AllowedForCreateUser.class);
        AllowedForUpdateUser allowedForUpdateUserAnnotation = writer.findAnnotation(AllowedForUpdateUser.class);
        if (allowedForCreateUserAnnotation == null && allowedForUpdateUserAnnotation == null) {
            return true;
        } else if (allowedForCreateUserAnnotation == null) {
            return checkAllowedForUpdateUser((BaseEntity) pojo);
        } else if (allowedForUpdateUserAnnotation == null) {
            return checkAllowedForCreateUser((BaseEntity) pojo);
        } else {
            BaseEntity entity = (BaseEntity) pojo;
            return checkAllowedForCreateUser(entity) || checkAllowedForUpdateUser(entity);
        }
    }

    private boolean checkAllowedForCreateUser(BaseEntity entity) {
        return Objects.equals(BaseContextHolder.getContext().getUserId(), entity.getCreateUser());
    }

    private boolean checkAllowedForUpdateUser(BaseEntity entity) {
        return Objects.equals(BaseContextHolder.getContext().getUserId(), entity.getUpdateUser());
    }
}
