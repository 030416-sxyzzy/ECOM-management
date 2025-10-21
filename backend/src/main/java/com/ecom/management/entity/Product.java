package com.ecom.management.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体
 */
@Data
public class Product {

    /** 商品ID */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 商品描述 */
    private String description;

    /** 价格 */
    private BigDecimal price;

    /** 库存数量 */
    private Integer stock;

    /** 分类ID */
    private Long categoryId;

    /** 图片URL */
    private String imageUrl;

    /** 状态：active/inactive */
    private String status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
