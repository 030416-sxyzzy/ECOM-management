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
     * @param orderId 订单号或订单ID
     * @param status 订单状态
     */
    @GetMapping
    public Result<List<OrderVO>> getAllOrders(
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String status) {
        List<OrderVO> orders = orderService.getAllOrders();
        
        // 智能搜索：如果是纯数字，按订单ID精确匹配；否则按订单号前缀匹配
        if (orderId != null && !orderId.isEmpty()) {
            // 判断是否为纯数字
            if (orderId.matches("\\d+")) {
                // 纯数字：按订单ID精确匹配
                final Long orderIdLong = Long.parseLong(orderId);
                orders = orders.stream()
                        .filter(order -> order.getId().equals(orderIdLong))
                        .collect(java.util.stream.Collectors.toList());
            } else {
                // 非纯数字：按订单号前缀匹配
                orders = orders.stream()
                        .filter(order -> order.getOrderNumber().startsWith(orderId.toUpperCase()))
                        .collect(java.util.stream.Collectors.toList());
            }
        }
        
        // 状态筛选
        if (status != null && !status.isEmpty()) {
            orders = orders.stream()
                    .filter(order -> status.equals(order.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        }
        
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