package com.ecom.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecom.management.dto.ProductSearchRequest;
import com.ecom.management.entity.Category;
import com.ecom.management.entity.Product;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {
    
    /**
     * 获取所有分类
     */
    List<Category> getAllCategories();
    
    /**
     * 根据分类ID获取商品列表
     */
    Page<Product> getProductsByCategory(Long categoryId, Integer page, Integer size);
    
    /**
     * 搜索商品
     */
    Page<Product> searchProducts(ProductSearchRequest request);
    
    /**
     * 获取商品详情
     */
    Product getProductById(Long productId);
    
    /**
     * 获取热门商品
     */
    List<Product> getHotProducts(Integer limit);
    
    /**
     * 获取推荐商品
     */
    List<Product> getRecommendedProducts(Long userId, Integer limit);
}
