package com.ecom.management.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 库存变更日志实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("inventory_logs")
public class InventoryLog {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 变更类型：in-入库, out-出库
     */
    private String changeType;
    
    /**
     * 变更数量
     */
    private Integer quantity;
    
    /**
     * 变更原因
     */
    private String reason;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
