package com.ecom.management.service;

import com.ecom.management.dto.PageResult;
import com.ecom.management.entity.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    Product create(Product product);

    boolean deleteById(Long id);

    Product update(Product product);

    Product findById(Long id);

    PageResult<Product> pageList(String nameKeyword, Long categoryId, String status, int page, int size);

    List<Product> listByCategory(Long categoryId);
    
    List<Product> searchProducts(Long categoryId, String keyword, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortOrder, int page, int pageSize);
    
    List<Product> getHotProducts(int limit);
    
    List<Product> getRecommendedProducts(int limit);
}


