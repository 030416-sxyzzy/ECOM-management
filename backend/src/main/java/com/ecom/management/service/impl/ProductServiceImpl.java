package com.ecom.management.service.impl;

import com.ecom.management.dto.PageResult;
import com.ecom.management.entity.Product;
import com.ecom.management.mapper.ProductMapper;
import com.ecom.management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public Product create(Product product) {
        productMapper.insert(product);
        return product;
    }

    @Override
    public boolean deleteById(Long id) {
        return productMapper.deleteById(id) > 0;
    }

    @Override
    public Product update(Product product) {
        productMapper.update(product);
        return productMapper.findById(product.getId());
    }

    @Override
    public Product findById(Long id) {
        return productMapper.findById(id);
    }

    @Override
    public PageResult<Product> pageList(String nameKeyword, Long categoryId, String status, int page, int size) {
        int offset = (page - 1) * size;
        List<Product> records = productMapper.findByPage(nameKeyword, categoryId, status, offset, size);
        int total = productMapper.countByPage(nameKeyword, categoryId, status);
        return new PageResult<>(records, total, page, size);
    }

    @Override
    public List<Product> listByCategory(Long categoryId) {
        return productMapper.findByCategory(categoryId);
    }

    @Override
    public List<Product> searchProducts(Long categoryId, String keyword, BigDecimal minPrice, BigDecimal maxPrice, 
                                       String sortBy, String sortOrder, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return productMapper.searchProducts(categoryId, keyword, minPrice, maxPrice, sortBy, sortOrder, offset, pageSize);
    }

    @Override
    public List<Product> getHotProducts(int limit) {
        return productMapper.findHotProducts(limit);
    }

    @Override
    public List<Product> getRecommendedProducts(int limit) {
        return productMapper.findRecommendedProducts(limit);
    }
}


