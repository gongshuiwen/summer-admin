package com.hzboiler.erp.core.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.hzboiler.erp.core.context.BaseContextHolder;
import com.hzboiler.erp.core.model.BaseModel;
import com.hzboiler.erp.core.security.Constants;
import com.hzboiler.erp.core.security.GrantedAuthorityCheckUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Objects;

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
        } else if (allowedForRolesAnnotation == null) {
            return checkAllowedForAdmin(allowedForAdminAnnotation);
        } else {
            return checkAllowedForAdmin(allowedForAdminAnnotation) || checkAllowedForRoles(allowedForRolesAnnotation);
        }
    }

    private boolean checkAllowedForAdmin(AllowedForAdmin allowedForAdminAnnotation) {
        if (allowedForAdminAnnotation != null) {
            return GrantedAuthorityCheckUtils.contains(new SimpleGrantedAuthority(Constants.ROLE_SYS_ADMIN));
        }
        return true;
    }

    private boolean checkAllowedForRoles(AllowedForRoles allowedForRolesAnnotation) {
        if (allowedForRolesAnnotation != null
                && allowedForRolesAnnotation.value() != null
                && allowedForRolesAnnotation.value().length > 0) {
            return GrantedAuthorityCheckUtils.containsAny(
                    Arrays.stream(allowedForRolesAnnotation.value())
                            .map(role -> new SimpleGrantedAuthority(Constants.ROLE_PREFIX + role))
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
            return checkAllowedForUpdateUser((BaseModel) pojo);
        } else if (allowedForUpdateUserAnnotation == null) {
            return checkAllowedForCreateUser((BaseModel) pojo);
        } else {
            BaseModel record = (BaseModel) pojo;
            return checkAllowedForCreateUser(record) || checkAllowedForUpdateUser(record);
        }
    }

    private boolean checkAllowedForCreateUser(BaseModel record) {
        return Objects.equals(BaseContextHolder.getContext().getUserId(), record.getCreateUser());
    }

    private boolean checkAllowedForUpdateUser(BaseModel record) {
        return Objects.equals(BaseContextHolder.getContext().getUserId(), record.getUpdateUser());
    }
}
