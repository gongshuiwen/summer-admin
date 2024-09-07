package io.summernova.admin.core.mapper;

import net.bytebuddy.ByteBuddy;
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

    // cache mapper interface, key: model class, value: mapper interface
    private static final Map<Class<?>, Class<?>> baseMapperInterfaceCache = new ConcurrentHashMap<>();

    // prevent external instantiation
    private BaseMapperRegistry() {
    }

    public static <T> BaseMapper<T> getBaseMapper(SqlSession sqlSession, Class<T> modelClass) {
        Configuration configuration = sqlSession.getConfiguration();
        Class<BaseMapper<T>> mapperInterface = getMapperInterface(modelClass);

        // add mapper to mybatis configuration
        if (!configuration.hasMapper(mapperInterface))
            configuration.addMapper(mapperInterface);

        return sqlSession.getMapper(mapperInterface);
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<BaseMapper<T>> getMapperInterface(Class<T> modelClass) {
        return (Class<BaseMapper<T>>) baseMapperInterfaceCache.computeIfAbsent(modelClass, BaseMapperRegistry::buildMapperInterface);
    }

    static Class<?> buildMapperInterface(Class<?> modelClass) {
        try (DynamicType.Unloaded<?> unloaded = new ByteBuddy()
                .makeInterface(TypeDescription.Generic.Builder.parameterizedType(BaseMapper.class, modelClass).build())
                .make()) {
            return unloaded.load(modelClass.getClassLoader(), ClassLoadingStrategy.Default.INJECTION).getLoaded();
        }
    }
}
