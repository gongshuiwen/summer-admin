package io.summernova.admin.core.mapper;

import io.summernova.admin.core.field.annotations.Many2ManyField;
import io.summernova.admin.core.model.BaseModel;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.summernova.admin.core.field.util.RelationFieldUtil.getTargetModelClass;

/**
 * @author gongshuiwen
 */
public final class RelationMapperRegistry {

    private static final ByteBuddy byteBuddy = new ByteBuddy();

    // cache mapper interfaces
    private static final Map<Key, Class<?>> relationMapperInterfaceCache = new ConcurrentHashMap<>();

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

        Key key = new Key(sourceClass, targetClass, many2ManyField.sourceField(), many2ManyField.targetField(),
                many2ManyField.joinTable());

        @SuppressWarnings("unchecked")
        Class<RelationMapper> mapperInterface = (Class<RelationMapper>) getMapperInterface(key);

        Configuration configuration = sqlSession.getConfiguration();
        if (!configuration.hasMapper(mapperInterface))
            configuration.addMapper(mapperInterface);

        return sqlSession.getMapper(mapperInterface);
    }

    private static Class<?> getMapperInterface(Key key) {
        return relationMapperInterfaceCache.computeIfAbsent(key,
                key1 -> buildMapperInterface(key1.sourceClass, key1.targetClass, key1.sourceField, key1.targetField, key1.joinTable));
    }

    static Class<?> buildMapperInterface(Class<?> sourceClass, Class<?> targetClass,
                                         String sourField, String targetField, String joinTable) {
        // Define the annotation
        AnnotationDescription annotation = AnnotationDescription.Builder.ofType(RelationMapperInfo.class)
                .define("class1", TypeDescription.ForLoadedType.of(sourceClass))
                .define("class2", TypeDescription.ForLoadedType.of(targetClass))
                .define("table", joinTable)
                .define("field1", sourField)
                .define("field2", targetField)
                .build();

        // Create the dynamic class
        try (DynamicType.Unloaded<?> unloaded = byteBuddy
                .makeInterface(RelationMapper.class)
                .annotateType(annotation)
                .make()) {
            return unloaded.load(RelationMapperRegistry.class.getClassLoader(), ClassLoadingStrategy.Default.INJECTION).
                    getLoaded();
        }
    }

    record Key(Class<?> sourceClass, Class<?> targetClass, String sourceField, String targetField, String joinTable) {
    }
}
