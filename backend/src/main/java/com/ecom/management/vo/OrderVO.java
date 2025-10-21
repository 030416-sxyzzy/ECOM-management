package com.ecom.management.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单视图对象
 */
@Data
public class OrderVO {
    private Long id;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String status;
    private String statusName;
    private String shippingAddress;
    private LocalDateTime createdAt;
    private List<OrderItemVO> items;
}