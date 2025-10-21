package com.ecom.management.vo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单详情视图对象
 */
@Data
public class OrderItemVO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}