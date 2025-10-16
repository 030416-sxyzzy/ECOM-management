package com.ecom.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecom.management.entity.Product;
import com.ecom.management.mapper.ProductMapper;
import com.ecom.management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        productMapper.updateById(product);
        return productMapper.selectById(product.getId());
    }

    @Override
    public Product findById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public Page<Product> pageList(String nameKeyword, Long categoryId, String status, int page, int size) {
        QueryWrapper<Product> query = new QueryWrapper<>();
        if (nameKeyword != null && !nameKeyword.isEmpty()) {
            query.like("name", nameKeyword);
        }
        if (categoryId != null) {
            query.eq("category_id", categoryId);
        }
        if (status != null && !status.isEmpty()) {
            query.eq("status", status);
        }
        query.orderByDesc("updated_at");
        Page<Product> pageObj = new Page<>(page, size);
        return productMapper.selectPage(pageObj, query);
    }

    @Override
    public List<Product> listByCategory(Long categoryId) {
        QueryWrapper<Product> query = new QueryWrapper<>();
        if (categoryId != null) {
            query.eq("category_id", categoryId);
        }
        query.eq("status", "active");
        query.orderByDesc("updated_at");
        return productMapper.selectList(query);
    }
}


