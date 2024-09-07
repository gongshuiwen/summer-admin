package io.summernova.admin.core.mapper;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Arrays;

/**
 * @author gongshuiwen
 */
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

            // cache mapperRelation
            RelationMapper.mapperRelationCache.put(mapperInterface, mapperRelation);

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
