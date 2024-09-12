package io.summernova.admin.spring.web;

import io.summernova.admin.core.service.AbstractBaseService;
import io.summernova.admin.core.service.BaseService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

@Service
public class MockService extends AbstractBaseService<Mock> implements BaseService<Mock> {
}
