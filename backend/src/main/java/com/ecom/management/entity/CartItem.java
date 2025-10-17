package com.ecom.management.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 购物车项实体
 */
@Data
public class CartItem {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
