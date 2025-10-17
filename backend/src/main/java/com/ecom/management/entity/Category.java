package com.ecom.management.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品分类实体
 */
@Data
public class Category {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
