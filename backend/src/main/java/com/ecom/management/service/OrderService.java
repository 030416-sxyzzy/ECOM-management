package com.ecom.management.service;

import com.ecom.management.entity.Order;
import com.ecom.management.dto.OrderDTO;
import com.ecom.management.vo.OrderVO;
import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     * @param orderDTO 订单数据传输对象
     * @return 生成的订单ID
     * @throws Exception 库存不足等异常
     */
    Long createOrder(OrderDTO orderDTO) throws Exception;

    /**
     * 查询用户所有订单
     * @param userId 用户ID
     * @return 订单列表
     */
    List<OrderVO> getUserOrders(Long userId);

    /**
     * 查询订单详情
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderVO getOrderDetail(Long orderId);

    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 是否更新成功
     */
    boolean updateOrderStatus(Long orderId, String status);
    
    /**
     * 查询所有订单
     * @return 订单列表
     */
    List<Order> getAllOrders();
}