package com.ecom.management.mapper;

import com.ecom.management.entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 订单详情Mapper接口
 */
@Mapper
public interface OrderItemMapper {
    
    @Insert("INSERT INTO order_items (order_id, product_id, quantity, price, created_at) " +
            "VALUES (#{orderId}, #{productId}, #{quantity}, #{price}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderItem orderItem);
    
    @Select("SELECT * FROM order_items WHERE order_id=#{orderId}")
    List<OrderItem> findByOrderId(Long orderId);
    
    @Insert("<script>" +
            "INSERT INTO order_items (order_id, product_id, quantity, price, created_at) VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.orderId}, #{item.productId}, #{item.quantity}, #{item.price}, NOW())" +
            "</foreach>" +
            "</script>")
    int batchInsert(List<OrderItem> items);
}