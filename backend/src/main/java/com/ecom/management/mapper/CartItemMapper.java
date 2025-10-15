package com.ecom.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecom.management.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 购物车Mapper接口
 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
    
    /**
     * 获取用户购物车商品详情（包含商品信息）
     */
    @Select("SELECT ci.*, p.name as product_name, p.price, p.image_url, p.stock, " +
            "c.name as category_name " +
            "FROM cart_items ci " +
            "LEFT JOIN products p ON ci.product_id = p.id " +
            "LEFT JOIN categories c ON p.category_id = c.id " +
            "WHERE ci.user_id = #{userId} " +
            "ORDER BY ci.created_at DESC")
    List<CartItem> getCartItemsWithDetails(@Param("userId") Long userId);
    
    /**
     * 检查商品是否已在购物车中
     */
    @Select("SELECT * FROM cart_items WHERE user_id = #{userId} AND product_id = #{productId}")
    CartItem findByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
