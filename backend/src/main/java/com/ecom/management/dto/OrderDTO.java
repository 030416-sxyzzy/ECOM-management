package com.ecom.management.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单数据传输对象
 */
@Data
public class OrderDTO {
    private Long userId;
    private String shippingAddress;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalAmount;
}