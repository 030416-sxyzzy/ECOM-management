package com.ecom.management.service;

import com.ecom.management.dto.AddToCartRequest;
import com.ecom.management.dto.CartItemVO;
import com.ecom.management.dto.UpdateCartRequest;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {
    
    /**
     * 获取用户购物车
     */
    List<CartItemVO> getCartItems(Long userId);
    
    /**
     * 添加商品到购物车
     */
    CartItemVO addToCart(Long userId, AddToCartRequest request);
    
    /**
     * 更新购物车商品数量
     */
    CartItemVO updateCartItem(Long userId, Long itemId, UpdateCartRequest request);
    
    /**
     * 删除购物车商品
     */
    void removeFromCart(Long userId, Long itemId);
    
    /**
     * 清空购物车
     */
    void clearCart(Long userId);
    
    /**
     * 获取购物车商品数量
     */
    Integer getCartItemCount(Long userId);
    
    /**
     * 获取购物车总金额
     */
    Double getCartTotalAmount(Long userId);
}
