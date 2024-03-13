package com.hzhg.plm.core.jackson2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.hzhg.plm.core.annotations.AllowedForRoles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleBasedAnnotationFilter extends SimpleBeanPropertyFilter {

    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        AllowedForRoles annotation = writer.findAnnotation(AllowedForRoles.class);
        if (annotation == null) {
            super.serializeAsField(pojo, jgen, provider, writer);
        } else {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails user = (UserDetails) principal;
                List<String> allowedRoles = Arrays.stream(annotation.value()).map(x -> ROLE_PREFIX + x).collect(Collectors.toList());
                Set<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                if (allowedRoles.stream().anyMatch(authorities::contains)) {
                    writer.serializeAsField(pojo, jgen, provider);
                }
            }
        }
    }
}
