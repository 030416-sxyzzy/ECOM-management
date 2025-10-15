package com.ecom.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecom.management.dto.ProductSearchRequest;
import com.ecom.management.entity.Category;
import com.ecom.management.entity.Product;
import com.ecom.management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    /**
     * 获取所有分类
     */
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = productService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    /**
     * 根据分类获取商品列表
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Product> products = productService.getProductsByCategory(categoryId, page, size);
        return ResponseEntity.ok(products);
    }
    
    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(ProductSearchRequest request) {
        Page<Product> products = productService.searchProducts(request);
        return ResponseEntity.ok(products);
    }
    
    /**
     * 获取商品详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
    
    /**
     * 获取热门商品
     */
    @GetMapping("/hot")
    public ResponseEntity<List<Product>> getHotProducts(
            @RequestParam(defaultValue = "10") Integer limit) {
        List<Product> products = productService.getHotProducts(limit);
        return ResponseEntity.ok(products);
    }
    
    /**
     * 获取推荐商品
     */
    @GetMapping("/recommended")
    public ResponseEntity<List<Product>> getRecommendedProducts(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<Product> products = productService.getRecommendedProducts(userId, limit);
        return ResponseEntity.ok(products);
    }
}
