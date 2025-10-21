package com.ecom.management.mapper;

import com.ecom.management.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper {

    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT id, email, username, password, role, status, created_at, updated_at FROM users WHERE email = #{email}")
    User findByEmail(String email);

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT id, email, username, password, role, status, created_at, updated_at FROM users WHERE username = #{username}")
    User findByUsername(String username);

    /**
     * 根据ID查询用户
     */
    @Select("SELECT id, email, username, password, role, status, created_at, updated_at FROM users WHERE id = #{id}")
    User findById(Long id);

    /**
     * 创建用户
     */
    @Insert("INSERT INTO users (email, username, password, role, status) VALUES (#{email}, #{username}, #{password}, #{role}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 更新用户
     */
    @Update("UPDATE users SET email = #{email}, username = #{username}, role = #{role}, status = #{status} WHERE id = #{id}")
    int update(User user);

    /**
     * 更新密码
     */
    @Update("UPDATE users SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 删除用户
     */
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(Long id);
}

