package com.ecom.management.entity;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品销量统计实体类（对应视图 v_product_sales_stats）
 */
@Data
public class ProductSalesStats {
    /** 商品ID */
    private Long productId;
    
    /** 商品名称 */
    private String productName;
    
    /** 当前价格 */
    private BigDecimal currentPrice;
    
    /** 总销售数量 */
    private Integer totalSoldQuantity;
    
    /** 总销售金额 */
    private BigDecimal totalSalesAmount;
    
    /** 订单数量 */
    private Integer orderCount;
    
    /** 当前库存 */
    private Integer currentStock;
    
    /** 库存状态 */
    private String stockStatus;
}

