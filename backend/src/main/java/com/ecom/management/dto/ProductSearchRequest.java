package com.ecom.management.dto;

import lombok.Data;

/**
 * 商品搜索请求DTO
 */
@Data
public class ProductSearchRequest {
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 价格范围 - 最低价
     */
    private Double minPrice;
    
    /**
     * 价格范围 - 最高价
     */
    private Double maxPrice;
    
    /**
     * 排序方式：price_asc, price_desc, created_at_desc, sales_desc
     */
    private String sortBy;
    
    /**
     * 页码
     */
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
}
