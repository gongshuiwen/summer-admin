package com.hzboiler.erp.core.mapper;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.hzboiler.erp.core.mapper.RelationMapper.*;

/**
 * @author gongshuiwen
 */
@Component
public class MapperBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof MapperFactoryBean<?> mapperFactoryBean) {
            Class<?> mapperInterface = mapperFactoryBean.getMapperInterface();
            if (mapperInterface == null) {
                return bean;
            }

            // check @MapperRelation annotation
            MapperRelation mapperRelation = mapperInterface.getAnnotation(MapperRelation.class);
            if (mapperRelation == null) {
                return bean;
            }

            // check RelationMapper interface
            Class<?> relationMapperInterface = Arrays.stream(mapperInterface.getInterfaces())
                    .filter(x -> x == RelationMapper.class).findFirst().orElse(null);
            if (relationMapperInterface == null) {
                return bean;
            }

            // cache table, field1, class1, filed2, class2
            mapperTables.put(mapperInterface, mapperRelation.table());
            mapperField1.put(mapperInterface, mapperRelation.field1());
            mapperClass1.put(mapperInterface, mapperRelation.class1());
            mapperField2.put(mapperInterface, mapperRelation.field2());
            mapperClass2.put(mapperInterface, mapperRelation.class2());

            // register RelationMapper
            try {
                RelationMapperRegistry.register(mapperRelation.class1(), mapperRelation.class2(), mapperFactoryBean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return bean;
    }
}
