package io.summernova.admin.core.query.adapter;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.summernova.admin.core.model.Mock;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.ParameterizedType;

/**
 * @author gongshuiwen
 */
abstract class QueryWrapperAdapterTestBase<A extends QueryWrapperAdapter<?>> {

    final A queryWrapperAdapter;
    QueryWrapper<Mock> mockQueryWrapper;

    @SuppressWarnings("unchecked")
    QueryWrapperAdapterTestBase() {
        // create A instance by reflection, then set it to queryWrapperAdapter
        try {
            Class<?> actualType = (Class<?>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            this.queryWrapperAdapter = (A) actualType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void beforeEach() {
        mockQueryWrapper = new QueryWrapper<>(Mock.class);
    }
}
