package io.summernova.admin.core.dal.mapper;

import io.summernova.admin.core.domain.annotations.Many2ManyField;
import io.summernova.admin.core.domain.model.BaseModel;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.summernova.admin.core.domain.util.RelationFieldUtil.getTargetModelClass;

/**
 * @author gongshuiwen
 */
public final class RelationMapperRegistry {

    private static final ByteBuddy byteBuddy = new ByteBuddy();

    private static final Map<RelationMapperInfo, Class<?>> relationMapperInterfaceCache = new ConcurrentHashMap<>();
    private static final Map<Class<?>, RelationMapperInfo> relationMapperInfoCache = new ConcurrentHashMap<>();

    // prevent external instantiation
    private RelationMapperRegistry() {
    }

    public static RelationMapper getRelationMapper(SqlSession sqlSession, Field field) {
        Many2ManyField many2ManyField = field.getDeclaredAnnotation(Many2ManyField.class);
        if (many2ManyField == null)
            throw new RuntimeException("Cannot get annotation @Many2ManyField for field '" +
                    field.getName() + "' in class '" + field.getDeclaringClass().getName() + "'");

        Class<?> sourceClass = field.getDeclaringClass();
        @SuppressWarnings("unchecked")
        Class<?> targetClass = getTargetModelClass((Class<? extends BaseModel>) sourceClass, field);

        RelationMapperInfo key = new RelationMapperInfo(sourceClass, targetClass, many2ManyField.sourceField(), many2ManyField.targetField(),
                many2ManyField.joinTable());

        return getRelationMapper(sqlSession, key);
    }

    public static RelationMapper getRelationMapper(SqlSession sqlSession, RelationMapperInfo relationMapperInfo) {
        @SuppressWarnings("unchecked")
        Class<RelationMapper> mapperInterface = (Class<RelationMapper>) getRelationMapperInterface(relationMapperInfo);

        Configuration configuration = sqlSession.getConfiguration();
        if (!configuration.hasMapper(mapperInterface))
            configuration.addMapper(mapperInterface);

        return sqlSession.getMapper(mapperInterface);
    }

    public static RelationMapperInfo getRelationMapperInfo(Class<?> relationMapperInterface) {
        return relationMapperInfoCache.get(relationMapperInterface);
    }

    static Class<?> getRelationMapperInterface(RelationMapperInfo relationMapperInfo) {
        return relationMapperInterfaceCache.computeIfAbsent(relationMapperInfo,
                info -> {
                    Class<?> mapperInterface = buildRelationMapperInterface();
                    relationMapperInfoCache.put(mapperInterface, info);
                    return mapperInterface;
                });
    }

    static Class<?> buildRelationMapperInterface() {
        // Create the dynamic class
        try (DynamicType.Unloaded<?> unloaded = byteBuddy
                .makeInterface(RelationMapper.class)
                .make()) {
            return unloaded.load(RelationMapperRegistry.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION).
                    getLoaded();
        }
    }
}
