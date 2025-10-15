package com.ecom.management.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车商品视图对象
 */
@Data
public class CartItemVO {
    
    /**
     * 购物车项ID
     */
    private Long id;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品价格
     */
    private BigDecimal price;
    
    /**
     * 商品图片
     */
    private String imageUrl;
    
    /**
     * 商品库存
     */
    private Integer stock;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 小计金额
     */
    private BigDecimal subtotal;
    
    /**
     * 添加时间
     */
    private LocalDateTime createdAt;
}
