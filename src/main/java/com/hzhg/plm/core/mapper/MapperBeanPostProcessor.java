package com.hzhg.plm.core.mapper;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.Arrays;

import static com.hzhg.plm.core.mapper.RelationMapper.*;

@Component
public class MapperBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof MapperFactoryBean<?> mapperFactoryBean) {
            Class<?> mapperInterface = mapperFactoryBean.getMapperInterface();
            MapperRelation mapperRelation = mapperInterface.getAnnotation(MapperRelation.class);
            Class<?>[] interfaces = mapperInterface.getInterfaces();
            Class<?> relationMapperInterface = Arrays.stream(interfaces)
                    .filter(x -> x == RelationMapper.class).findFirst().orElse(null);
            if (mapperRelation != null && relationMapperInterface != null) {

                // cache table, field1 and filed2
                mapperTables.put(mapperInterface, mapperRelation.table());
                mapperField1.put(mapperInterface, mapperRelation.field1());
                mapperField2.put(mapperInterface, mapperRelation.field2());

                // Cache actualTypeArgument1 and actualTypeArgument2
                Type[] actualTypeArguments = ((ParameterizedTypeImpl) mapperInterface.getGenericInterfaces()[0]).getActualTypeArguments();
                mapperActualTypeArgument1.put(mapperInterface, (Class<?>) actualTypeArguments[0]);
                mapperActualTypeArgument2.put(mapperInterface, (Class<?>) actualTypeArguments[1]);

                try {
                    RelationMapperRegistry.register(
                            (Class<?>) actualTypeArguments[0],
                            (Class<?>) actualTypeArguments[1],
                            mapperFactoryBean);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return bean;
    }
}
