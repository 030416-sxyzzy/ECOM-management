package com.ecom.management.service.impl;

import com.ecom.management.entity.Order;
import com.ecom.management.entity.OrderItem;
import com.ecom.management.entity.Product;
import com.ecom.management.dto.OrderDTO;
import com.ecom.management.dto.OrderItemDTO;
import com.ecom.management.vo.OrderVO;
import com.ecom.management.vo.OrderItemVO;
import com.ecom.management.enums.OrderStatus;
import com.ecom.management.mapper.OrderMapper;
import com.ecom.management.mapper.OrderItemMapper;
import com.ecom.management.mapper.ProductMapper;
import com.ecom.management.mapper.CartItemMapper;
import com.ecom.management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

/**
 * 订单服务实现类
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final CartItemMapper cartItemMapper;

    /**
     * 创建订单（事务控制）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(OrderDTO orderDTO) throws Exception {
        // 1. 生成订单号
        String orderNumber = generateOrderNumber();

        // 2. 检查库存
        for (OrderItemDTO item : orderDTO.getOrderItems()) {
            Product product = productMapper.findById(item.getProductId());
            if (product == null) {
                throw new Exception("商品不存在：" + item.getProductId());
            }
            if (product.getStock() < item.getQuantity()) {
                throw new Exception("商品库存不足：" + product.getName() + "，当前库存：" + product.getStock());
            }
        }

        // 3. 创建订单主表记录
        Order order = new Order();
        order.setUserId(orderDTO.getUserId());
        order.setOrderNumber(orderNumber);
        order.setTotalAmount(orderDTO.getTotalAmount());
        order.setStatus(OrderStatus.PENDING.getCode());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        // 4. 创建订单详情记录（触发库存扣减触发器）
        for (OrderItemDTO item : orderDTO.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItem.setCreatedAt(LocalDateTime.now());
            orderItemMapper.insert(orderItem);
        }

        // 5. 清空用户购物车
        cartItemMapper.deleteByUserId(orderDTO.getUserId());

        return order.getId();
    }

    /**
     * 查询用户所有订单
     */
    @Override
    public List<OrderVO> getUserOrders(Long userId) {
        List<Order> orders = orderMapper.findByUserId(userId);
        return orders.stream().map(this::convertToOrderVO).collect(Collectors.toList());
    }

    /**
     * 查询订单详情
     */
    @Override
    public OrderVO getOrderDetail(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order == null) {
            return null;
        }
        return convertToOrderVO(order);
    }

    /**
     * 更新订单状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderStatus(Long orderId, String status) {
        return orderMapper.updateStatus(orderId, status) > 0;
    }
    
    /**
     * 查询所有订单
     */
    @Override
    public List<Order> getAllOrders() {
        return orderMapper.findAll();
    }

    /**
     * 生成订单号
     */
    private String generateOrderNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "ORD" + timestamp + random;
    }

    /**
     * 转换为订单VO
     */
    private OrderVO convertToOrderVO(Order order) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setOrderNumber(order.getOrderNumber());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus());
        vo.setStatusName(OrderStatus.getByCode(order.getStatus()).getDescription());
        vo.setShippingAddress(order.getShippingAddress());
        vo.setCreatedAt(order.getCreatedAt());

        // 查询订单详情
        List<OrderItem> items = orderItemMapper.findByOrderId(order.getId());

        List<OrderItemVO> itemVOs = items.stream().map(item -> {
            OrderItemVO itemVO = new OrderItemVO();
            itemVO.setId(item.getId());
            itemVO.setProductId(item.getProductId());
            itemVO.setQuantity(item.getQuantity());
            itemVO.setPrice(item.getPrice());

            // 查询商品名称
            Product product = productMapper.findById(item.getProductId());
            if (product != null) {
                itemVO.setProductName(product.getName());
            }
            return itemVO;
        }).collect(Collectors.toList());

        vo.setItems(itemVOs);
        return vo;
    }
}