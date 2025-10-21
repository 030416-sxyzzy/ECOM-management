package com.ecom.management.mapper;

import com.ecom.management.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper {
    
    @Insert("INSERT INTO orders (user_id, order_number, total_amount, status, shipping_address, receiver_name, receiver_phone, receiver_address, created_at, updated_at) " +
            "VALUES (#{userId}, #{orderNumber}, #{totalAmount}, #{status}, #{shippingAddress}, #{receiverName}, #{receiverPhone}, #{receiverAddress}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);
    
    @Update("UPDATE orders SET status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    
    @Select("SELECT * FROM orders WHERE id=#{id}")
    Order findById(Long id);
    
    @Select("SELECT * FROM orders WHERE user_id=#{userId} ORDER BY created_at DESC")
    List<Order> findByUserId(Long userId);
    
    @Select("SELECT * FROM orders ORDER BY created_at DESC")
    List<Order> findAll();
}