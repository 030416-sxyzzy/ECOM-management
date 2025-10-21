package com.ecom.management.controller;

import com.ecom.management.common.Result;
import com.ecom.management.entity.User;
import com.ecom.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String username = request.get("username");
            String password = request.get("password");
            String role = request.get("role"); // 用户选择的角色：USER 或 ADMIN

            // 参数校验
            if (email == null || email.trim().isEmpty()) {
                return Result.error("邮箱不能为空");
            }
            if (username == null || username.trim().isEmpty()) {
                return Result.error("用户名不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                return Result.error("密码不能为空");
            }
            if (role == null || (!role.equals("USER") && !role.equals("ADMIN"))) {
                return Result.error("请选择用户角色");
            }

            User user = userService.register(email, username, password, role);

            return Result.success(user, "注册成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");

            // 参数校验
            if (email == null || email.trim().isEmpty()) {
                return Result.error("邮箱不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                return Result.error("密码不能为空");
            }

            User user = userService.login(email, password);

            // 返回用户信息和token（这里简化处理，实际项目应使用JWT）
            Map<String, Object> data = new HashMap<>();
            data.put("user", user);
            data.put("token", "mock-token-" + user.getId()); // 模拟token

            return Result.success(data, "登录成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public Result<User> getCurrentUser(@RequestParam Long userId) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 模拟发送验证码（已删除，不再需要）
     * 保留接口避免前端报错，但直接返回成功
     */
    @PostMapping("/send-code")
    public Result<String> sendCode(@RequestBody Map<String, String> request) {
        return Result.success("验证码功能已移除");
    }
}

