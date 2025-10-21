package com.ecom.management.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单详情数据传输对象
 */
@Data
public class OrderItemDTO {
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private String productName; // 冗余字段，用于前端展示
}