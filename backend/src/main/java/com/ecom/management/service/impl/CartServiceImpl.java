package com.ecom.management.service.impl;

import com.ecom.management.dto.AddToCartRequest;
import com.ecom.management.dto.CartItemVO;
import com.ecom.management.dto.UpdateCartRequest;
import com.ecom.management.entity.CartItem;
import com.ecom.management.entity.Product;
import com.ecom.management.mapper.CartItemMapper;
import com.ecom.management.mapper.ProductMapper;
import com.ecom.management.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    
    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;
    
    @Override
    public List<CartItemVO> getCartItems(Long userId) {
        List<CartItem> cartItems = cartItemMapper.getCartItemsWithDetails(userId);
        return cartItems.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public CartItemVO addToCart(Long userId, AddToCartRequest request) {
        // 检查商品是否存在且上架
        Product product = productMapper.selectById(request.getProductId());
        if (product == null || !"active".equals(product.getStatus())) {
            throw new RuntimeException("商品不存在或已下架");
        }
        
        // 检查库存
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("库存不足");
        }
        
        // 检查商品是否已在购物车中
        CartItem existingItem = cartItemMapper.findByUserAndProduct(userId, request.getProductId());
        
        if (existingItem != null) {
            // 更新数量
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setUpdatedAt(LocalDateTime.now());
            cartItemMapper.updateById(existingItem);
            return convertToVO(existingItem, product);
        } else {
            // 新增购物车项
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setCreatedAt(LocalDateTime.now());
            cartItem.setUpdatedAt(LocalDateTime.now());
            
            cartItemMapper.insert(cartItem);
            return convertToVO(cartItem, product);
        }
    }
    
    @Override
    @Transactional
    public CartItemVO updateCartItem(Long userId, Long itemId, UpdateCartRequest request) {
        // 检查购物车项是否存在且属于当前用户
        CartItem cartItem = cartItemMapper.selectById(itemId);
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("购物车项不存在");
        }
        
        // 检查商品库存
        Product product = productMapper.selectById(cartItem.getProductId());
        if (product == null || !"active".equals(product.getStatus())) {
            throw new RuntimeException("商品不存在或已下架");
        }
        
        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("库存不足");
        }
        
        // 更新数量
        cartItem.setQuantity(request.getQuantity());
        cartItem.setUpdatedAt(LocalDateTime.now());
        cartItemMapper.updateById(cartItem);
        
        return convertToVO(cartItem, product);
    }
    
    @Override
    @Transactional
    public void removeFromCart(Long userId, Long itemId) {
        // 检查购物车项是否存在且属于当前用户
        CartItem cartItem = cartItemMapper.selectById(itemId);
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("购物车项不存在");
        }
        
        cartItemMapper.deleteById(itemId);
    }
    
    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartItemMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<CartItem>()
                .eq("user_id", userId));
    }
    
    @Override
    public Integer getCartItemCount(Long userId) {
        List<CartItemVO> cartItems = getCartItems(userId);
        return cartItems.stream().mapToInt(CartItemVO::getQuantity).sum();
    }
    
    @Override
    public Double getCartTotalAmount(Long userId) {
        List<CartItemVO> cartItems = getCartItems(userId);
        return cartItems.stream()
                .mapToDouble(item -> item.getSubtotal().doubleValue())
                .sum();
    }
    
    /**
     * 转换为VO对象（从数据库查询结果）
     */
    private CartItemVO convertToVO(CartItem cartItem) {
        CartItemVO vo = new CartItemVO();
        vo.setId(cartItem.getId());
        vo.setProductId(cartItem.getProductId());
        vo.setQuantity(cartItem.getQuantity());
        vo.setCreatedAt(cartItem.getCreatedAt());
        
        // 这里需要从数据库查询商品信息，简化处理
        Product product = productMapper.selectById(cartItem.getProductId());
        if (product != null) {
            vo.setProductName(product.getName());
            vo.setPrice(product.getPrice());
            vo.setImageUrl(product.getImageUrl());
            vo.setStock(product.getStock());
            vo.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        
        return vo;
    }
    
    /**
     * 转换为VO对象（已知商品信息）
     */
    private CartItemVO convertToVO(CartItem cartItem, Product product) {
        CartItemVO vo = new CartItemVO();
        vo.setId(cartItem.getId());
        vo.setProductId(cartItem.getProductId());
        vo.setProductName(product.getName());
        vo.setPrice(product.getPrice());
        vo.setImageUrl(product.getImageUrl());
        vo.setStock(product.getStock());
        vo.setQuantity(cartItem.getQuantity());
        vo.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        vo.setCreatedAt(cartItem.getCreatedAt());
        return vo;
    }
}
