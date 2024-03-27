package com.hzhg.plm.core.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.hzhg.plm.core.annotations.AllowedForRoles;
import com.hzhg.plm.core.security.GrantedAuthorityCheckUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class RoleBasedAnnotationFilter extends SimpleBeanPropertyFilter {

    public static final String ROLE_BASED_FILTER_ID = "ROLE_BASED_FILTER";
    public static final SimpleBeanPropertyFilter ROLE_BASED_FILTER = new RoleBasedAnnotationFilter();
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        AllowedForRoles annotation = writer.findAnnotation(AllowedForRoles.class);
        if (annotation == null) {
            super.serializeAsField(pojo, jgen, provider, writer);
        } else {
            List<SimpleGrantedAuthority> authorities = Arrays.stream(annotation.value())
                    .map(x -> new SimpleGrantedAuthority(ROLE_PREFIX + x)).toList();
            if (GrantedAuthorityCheckUtils.containsAny(authorities)) {
                writer.serializeAsField(pojo, jgen, provider);
            }
        }
    }
}
