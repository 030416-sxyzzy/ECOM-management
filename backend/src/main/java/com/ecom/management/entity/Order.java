package com.ecom.management.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
public class Order {
    /** 订单ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 订单编号 */
    private String orderNumber;

    /** 总金额 */
    private BigDecimal totalAmount;

    /** 订单状态 */
    private String status;

    /** 配送地址 */
    private String shippingAddress;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}