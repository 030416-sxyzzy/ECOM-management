package com.ecom.management.service.impl;

import com.ecom.management.entity.User;
import com.ecom.management.mapper.UserMapper;
import com.ecom.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User register(String email, String username, String password, String role) {
        // 检查邮箱是否已存在
        if (userMapper.findByEmail(email) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 检查用户名是否已存在
        if (userMapper.findByUsername(username) != null) {
            throw new RuntimeException("用户名已被使用");
        }

        // 创建用户
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(encodePassword(password)); // 密码加密
        user.setRole(role); // 使用用户选择的角色
        user.setStatus(1); // 默认启用

        userMapper.insert(user);
        
        // 返回时不包含密码
        user.setPassword(null);
        return user;
    }

    @Override
    public User login(String email, String password) {
        // 根据邮箱查询用户
        User user = userMapper.findByEmail(email);
        
        if (user == null) {
            throw new RuntimeException("邮箱或密码错误");
        }

        // 检查状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }

        // 验证密码
        if (!encodePassword(password).equals(user.getPassword())) {
            throw new RuntimeException("邮箱或密码错误");
        }

        // 返回时不包含密码
        user.setPassword(null);
        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userMapper.findById(id);
        if (user != null) {
            user.setPassword(null); // 不返回密码
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = userMapper.findByEmail(email);
        if (user != null) {
            user.setPassword(null); // 不返回密码
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userMapper.findByUsername(username);
        if (user != null) {
            user.setPassword(null); // 不返回密码
        }
        return user;
    }

    /**
     * 密码加密（使用MD5+盐值）
     * 注意：实际项目应使用BCrypt等更安全的加密方式
     */
    private String encodePassword(String password) {
        String salt = "ecom_salt_2025"; // 盐值
        return DigestUtils.md5DigestAsHex((password + salt).getBytes(StandardCharsets.UTF_8));
    }
}

