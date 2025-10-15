package com.ecom.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecom.management.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品Mapper接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    
    /**
     * 根据关键词搜索商品
     */
    @Select("SELECT p.*, c.name as category_name FROM products p " +
            "LEFT JOIN categories c ON p.category_id = c.id " +
            "WHERE p.status = 'active' AND (p.name LIKE CONCAT('%', #{keyword}, '%') OR p.description LIKE CONCAT('%', #{keyword}, '%')) " +
            "ORDER BY p.created_at DESC")
    List<Product> searchProducts(@Param("keyword") String keyword);
    
    /**
     * 获取热门商品（按销量排序）
     */
    @Select("SELECT p.*, c.name as category_name, " +
            "COALESCE(SUM(oi.quantity), 0) as total_sold " +
            "FROM products p " +
            "LEFT JOIN categories c ON p.category_id = c.id " +
            "LEFT JOIN order_items oi ON p.id = oi.product_id " +
            "LEFT JOIN orders o ON oi.order_id = o.id AND o.status != 'cancelled' " +
            "WHERE p.status = 'active' " +
            "GROUP BY p.id " +
            "ORDER BY total_sold DESC, p.created_at DESC " +
            "LIMIT #{limit}")
    List<Product> getHotProducts(@Param("limit") Integer limit);
}
