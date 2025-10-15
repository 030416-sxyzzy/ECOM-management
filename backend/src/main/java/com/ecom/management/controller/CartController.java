package com.ecom.management.controller;

import com.ecom.management.dto.AddToCartRequest;
import com.ecom.management.dto.CartItemVO;
import com.ecom.management.dto.UpdateCartRequest;
import com.ecom.management.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    /**
     * 获取用户购物车
     */
    @GetMapping
    public ResponseEntity<List<CartItemVO>> getCartItems(@RequestParam Long userId) {
        List<CartItemVO> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }
    
    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    public ResponseEntity<CartItemVO> addToCart(
            @RequestParam Long userId,
            @Valid @RequestBody AddToCartRequest request) {
        try {
            CartItemVO cartItem = cartService.addToCart(userId, request);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 更新购物车商品数量
     */
    @PutMapping("/{itemId}")
    public ResponseEntity<CartItemVO> updateCartItem(
            @RequestParam Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartRequest request) {
        try {
            CartItemVO cartItem = cartService.updateCartItem(userId, itemId, request);
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 删除购物车商品
     */
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestParam Long userId,
            @PathVariable Long itemId) {
        try {
            cartService.removeFromCart(userId, itemId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 清空购物车
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * 获取购物车统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCartStats(@RequestParam Long userId) {
        Integer itemCount = cartService.getCartItemCount(userId);
        Double totalAmount = cartService.getCartTotalAmount(userId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("itemCount", itemCount);
        stats.put("totalAmount", totalAmount);
        
        return ResponseEntity.ok(stats);
    }
}
