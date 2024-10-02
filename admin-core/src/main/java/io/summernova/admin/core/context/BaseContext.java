package io.summernova.admin.core.context;

import io.summernova.admin.core.dal.mapper.*;
import io.summernova.admin.core.domain.annotations.Many2ManyField;
import io.summernova.admin.core.domain.model.BaseModel;
import io.summernova.admin.core.security.account.BaseUser;
import io.summernova.admin.core.security.authorization.BaseAuthority;
import io.summernova.admin.core.security.authorization.SimpleAuthority;
import io.summernova.admin.core.security.model.ModelAccessChecker;
import io.summernova.admin.core.security.model.ModelAccessException;
import io.summernova.admin.core.security.model.ModelAccessType;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static io.summernova.admin.core.domain.util.RelationFieldUtil.getTargetModelClass;

/**
 * BaseContext stores common data for every request of the application.
 * It is created when required, discarded after each request and never shared between threads and requests.
 *
 * @author gongshuiwen
 */
public interface BaseContext extends ModelAccessChecker {

    /**
     * Get the unique ID of the current request.
     *
     * @return the unique ID
     */
    String getId();

    /**
     * Get the user ID of the current request.
     *
     * @return the user ID
     */
    Long getUserId();

    /**
     * Get the user information of the current request.
     *
     * @return the user information or null if it is not available
     */
    <T extends BaseUser> T getUser();

    /**
     * Get the user authorities of the current request.
     *
     * @return an unmodifiable set of authorities or an empty set if they are not available
     */
    Set<? extends BaseAuthority> getAuthorities();

    /**
     * Check if the current user is anonymous.
     *
     * @return true if the user is anonymous, false otherwise.
     */
    boolean isAnonymous();

    /**
     * Check if the current user is a super admin.
     *
     * @return true if the user is a super admin, false otherwise.
     */
    boolean isSuperAdmin();

    /**
     * Check if the current user is an admin.
     *
     * @return true if the user is an admin, false otherwise.
     */
    boolean isAdmin();

    /**
     * Get the attributes map of the current request.
     *
     * @return the attributes map of the current request
     */
    Map<String, Object> getAttributes();

    /**
     * Get the value mapped to the given key from the attributes map.
     *
     * @param key the key whose mapped value is to be retrieved
     * @return the value mapped to the given key, or null if there was no mapping for the key
     */
    Object getAttribute(String key);

    /**
     * Set the value with the given key in the attributes map.
     *
     * @param key   the key whose mapped value is to be set
     * @param value the value to be set
     * @return the previous value mapped to the given key, or null if there was no mapping for the key
     */
    Object setAttribute(String key, Object value);

    /**
     * Remove the given key from the attributes map.
     *
     * @param key the key whose mapped value is to be removed
     * @return the value mapped to the given key, or null if there was no mapping for the key
     */
    Object removeAttribute(String key);

    /**
     * Get the {@link SqlSessionFactory} of the current request.
     *
     * @return the {@link SqlSessionFactory} of the current request
     */
    SqlSessionFactory getSqlSessionFactory();

    /**
     * Get the {@link SqlSession} of the current request.
     *
     * @return the {@link SqlSession} of the current request
     */
    SqlSession getSqlSession();

    /**
     * Get the instance for the given mapper interface
     *
     * @param mapperInterface the mapper interface
     * @param <M>             the mapper interface type
     * @return {@link BaseMapper} instance
     */
    default <M> M getMapper(Class<M> mapperInterface) {
        return getSqlSession().getMapper(mapperInterface);
    }

    /**
     * Get the {@link BaseMapper} instance for the given model class
     *
     * @param modelClass the model class
     * @param <T>        the model class type
     * @return {@link BaseMapper} instance
     */
    default <T extends BaseModel> BaseMapper<T> getBaseMapper(Class<T> modelClass) {
        return BaseMapperRegistry.getBaseMapper(getSqlSession(), modelClass);
    }

    /**
     * Get the {@link RelationMapper} instance for the given many2many field
     *
     * @param field the many2many field
     * @return {@link RelationMapper} instance
     */
    default RelationMapper getRelationMapper(Field field) {
        Many2ManyField many2ManyField = field.getDeclaredAnnotation(Many2ManyField.class);
        if (many2ManyField == null)
            throw new RuntimeException("Cannot get annotation @Many2ManyField for field '" +
                    field.getName() + "' in class '" + field.getDeclaringClass().getName() + "'");

        Class<?> sourceClass = field.getDeclaringClass();
        @SuppressWarnings("unchecked")
        Class<?> targetClass = getTargetModelClass((Class<? extends BaseModel>) sourceClass, field);

        RelationMapperInfo relationMapperInfo = new RelationMapperInfo(sourceClass, targetClass,
                many2ManyField.sourceField(), many2ManyField.targetField(), many2ManyField.joinTable());

        return RelationMapperRegistry.getRelationMapper(getSqlSession(), relationMapperInfo);
    }

    /**
     * Get the {@link HttpServletRequest} instance of the current request if in the web environment, else null.
     *
     * @return the {@link HttpServletRequest} instance of the current request
     */
    HttpServletRequest getHttpServletRequest();


    @Override
    default void checkModelAccess(Class<? extends BaseModel> modelClass, ModelAccessType modelAccessType) {
        Objects.requireNonNull(modelClass, "modelClass cannot be null");

        // SYS_ADMIN has access to everything
        if (isAdmin())
            return;

        // Otherwise, check if the user has the required authority
        BaseAuthority authorityRequired = SimpleAuthority.of(modelAccessType.getPrefix() + modelClass.getSimpleName());
        if (getAuthorities().contains(authorityRequired))
            return;

        throw new ModelAccessException(modelClass, modelAccessType);
    }
}
