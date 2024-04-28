package com.hzboiler.core.mapper;

import org.mybatis.spring.mapper.MapperFactoryBean;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RelationMapperRegistry {

    private static final Map<Key, MapperFactoryBean<?>> registry = new ConcurrentHashMap<>();

    public static void register(Class<?> class1, Class<?> class2, MapperFactoryBean<?> mapperFactoryBean) {
        Key key = new Key(class1, class2);
        registry.put(key, mapperFactoryBean);
    }

    public static RelationMapper getMapper(Class<?> class1, Class<?> class2) {
        Key key = new Key(class1, class2);
        try {
            return (RelationMapper) registry.get(key).getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class Key {
        private final Class<?> class1;
        private final Class<?> class2;

        public Key(Class<?> class1, Class<?> class2) {
            this.class1 = class1;
            this.class2 = class2;
        }

        @Override
        public int hashCode() {
            return class1.hashCode() + class2.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Key key)) {
                return false;
            }
            return Objects.equals(class1, key.class1) && Objects.equals(class2, key.class2);
        }
    }
}
