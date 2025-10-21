package com.ecom.management.service;

import com.ecom.management.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    User register(String email, String username, String password, String role);

    /**
     * 用户登录
     */
    User login(String email, String password);

    /**
     * 根据ID查询用户
     */
    User findById(Long id);

    /**
     * 根据邮箱查询用户
     */
    User findByEmail(String email);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);
}

