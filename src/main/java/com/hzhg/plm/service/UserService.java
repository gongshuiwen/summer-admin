package com.hzhg.plm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzhg.plm.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends IService<User>, UserDetailsService {
}
