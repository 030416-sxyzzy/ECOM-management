package com.ecom.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecom.management.entity.Product;

import java.util.List;

/**
 * 商品服务
 */
public interface ProductService {

    Product create(Product product);

    boolean deleteById(Long id);

    Product update(Product product);

    Product findById(Long id);

    Page<Product> pageList(String nameKeyword, Long categoryId, String status, int page, int size);

    List<Product> listByCategory(Long categoryId);
}


