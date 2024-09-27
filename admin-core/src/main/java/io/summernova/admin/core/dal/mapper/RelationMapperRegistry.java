package io.summernova.admin.core.dal.mapper;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
