package com.ecom.management.mapper;

import com.ecom.management.entity.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 购物车Mapper
 */
@Mapper
public interface CartItemMapper {
    
    @Select("SELECT * FROM cart_items WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<CartItem> findByUserId(Long userId);
    
    @Insert("INSERT INTO cart_items (user_id, product_id, quantity, created_at, updated_at) VALUES (#{userId}, #{productId}, #{quantity}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CartItem cartItem);
    
    @Update("UPDATE cart_items SET quantity = #{quantity}, updated_at = NOW() WHERE id = #{id}")
    int updateQuantity(@Param("id") Long id, @Param("quantity") Integer quantity);
    
    @Delete("DELETE FROM cart_items WHERE id = #{id}")
    int deleteById(Long id);
    
    @Delete("DELETE FROM cart_items WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
}
