package com.hzhg.plm.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.hzhg.plm.core.annotations.FetchName;
import com.hzhg.plm.core.annotations.AllowedForRoles;
import com.hzhg.plm.core.utils.SpringContextUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hzhg.plm.core.controller.BaseController.ROLE_ADMIN;


@Getter
@Setter
@JsonFilter("roleBasedAnnotationFilter")
public abstract class BaseEntity implements Serializable {

    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @AllowedForRoles(value = {ROLE_ADMIN})
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @AllowedForRoles(value = {ROLE_ADMIN})
    private LocalDateTime updateTime;

    @Schema(description = "创建用户")
    @TableField(fill = FieldFill.INSERT)
    @AllowedForRoles(value = {ROLE_ADMIN})
    private Long createUser;

    @Schema(description = "更新用户")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @AllowedForRoles(value = {ROLE_ADMIN})
    private Long updateUser;

    public abstract String getDisplayName();

    public static <T extends BaseEntity> void fetchNames(List<T> entities)
            throws NoSuchFieldException, IllegalAccessException {

        // Do noting if empty
        if ( entities.isEmpty() ) return;

        // Get entity class
        Class<?> clazz = entities.get(0).getClass();
        List<Field> fetchNameFields = getFetchNameFields(clazz);
        for (Field fetchNameField : fetchNameFields) {
            // Get annotation
            FetchName annotation = fetchNameField.getAnnotation(FetchName.class);

            // Get id field
            Field idField = clazz.getDeclaredField(annotation.idField());
            idField.setAccessible(true);

            // Get target mapper from application context
            Class<? extends BaseMapper<? extends BaseEntity>> mapperClazz = annotation.mapper();
            BaseMapper<? extends BaseEntity> mapper = SpringContextUtils.getApplicationContext().getBean(mapperClazz);
            for (T entity : entities) {
                // Use selectById to fetch target entity and get this display name
                // TODO: Optimize performance by cache
                BaseEntity t = mapper.selectById((Serializable) idField.get(entity));
                if (t != null) {
                    fetchNameField.set(entity, t.getDisplayName());
                }
            }
        }
    }

    private static List<Field> getFetchNameFields(Class<?> clazz) {
        // TODO: Optimize performance by cache
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(FetchName.class) != null)
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + getId() + "[" + getDisplayName() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BaseEntity)) return false;
        return Objects.equals(this.id, ((BaseEntity) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }
}
