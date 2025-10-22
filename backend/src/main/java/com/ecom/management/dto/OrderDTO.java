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
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private List<OrderItemDTO> orderItems;
    private BigDecimal totalAmount;
    private List<Long> cartItemIds; // 选中的购物车商品ID列表（用于结算后删除）
}