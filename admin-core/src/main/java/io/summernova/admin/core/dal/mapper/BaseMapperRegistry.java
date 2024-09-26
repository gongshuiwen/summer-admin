package io.summernova.admin.core.dal.mapper;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gongshuiwen
 */
public final class BaseMapperRegistry {

    private static final ByteBuddy byteBuddy = new ByteBuddy();

    // cache mapper interface, key: model class, value: mapper interface
    private static final Map<Class<?>, Class<?>> baseMapperInterfaceCache = new ConcurrentHashMap<>();

    // prevent external instantiation
    private BaseMapperRegistry() {
    }

    public static <T> BaseMapper<T> getBaseMapper(SqlSession sqlSession, Class<T> modelClass) {
        Configuration configuration = sqlSession.getConfiguration();

        @SuppressWarnings("unchecked")
        Class<BaseMapper<T>> mapperInterface = (Class<BaseMapper<T>>) getMapperInterface(modelClass);

        // add mapper to mybatis configuration
        if (!configuration.hasMapper(mapperInterface))
            configuration.addMapper(mapperInterface);

        return sqlSession.getMapper(mapperInterface);
    }

    private static Class<?> getMapperInterface(Class<?> modelClass) {
        return baseMapperInterfaceCache.computeIfAbsent(
                modelClass, BaseMapperRegistry::buildMapperInterface);
    }

    static Class<?> buildMapperInterface(Class<?> modelClass) {
        // Create TypeDefinition
        TypeDefinition typeDefinition = TypeDescription.Generic.Builder.parameterizedType(BaseMapper.class, modelClass).build();

        try (DynamicType.Unloaded<?> unloaded = byteBuddy
                .makeInterface(typeDefinition)
                .make()) {
            return unloaded.load(BaseMapperRegistry.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION).getLoaded();
        }
    }
}
