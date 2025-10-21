package com.ecom.management.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
public class User {
    /** 用户ID */
    private Long id;

    /** 邮箱 */
    private String email;

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    /** 角色：USER-普通用户，ADMIN-管理员 */
    private String role;

    /** 状态：0-禁用，1-启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}

