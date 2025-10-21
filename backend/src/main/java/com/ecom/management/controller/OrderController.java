package com.ecom.management.controller;

import com.ecom.management.common.Result;
import com.ecom.management.dto.OrderDTO;
import com.ecom.management.service.OrderService;
import com.ecom.management.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public Result<Long> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            Long orderId = orderService.createOrder(orderDTO);
            return Result.success(orderId, "订单创建成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取所有订单列表（管理员）
     */
    @GetMapping
    public Result<List<OrderVO>> getAllOrders() {
        List<OrderVO> orders = orderService.getAllOrders();
        return Result.success(orders);
    }

    /**
     * 获取用户订单列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<OrderVO>> getUserOrders(@PathVariable Long userId) {
        List<OrderVO> orders = orderService.getUserOrders(userId);
        return Result.success(orders);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{orderId}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long orderId) {
        OrderVO orderVO = orderService.getOrderDetail(orderId);
        return Result.success(orderVO);
    }

    /**
     * 更新订单状态
     */
    @PutMapping("/{orderId}/status")
    public Result<Boolean> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        boolean success = orderService.updateOrderStatus(orderId, status);
        return success ? Result.success(true) : Result.error("更新订单状态失败");
    }
}