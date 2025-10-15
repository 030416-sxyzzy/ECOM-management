package com.ecom.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecom.management.dto.ProductSearchRequest;
import com.ecom.management.entity.Category;
import com.ecom.management.entity.Product;
import com.ecom.management.mapper.CategoryMapper;
import com.ecom.management.mapper.ProductMapper;
import com.ecom.management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品服务实现类
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    
    @Override
    public List<Category> getAllCategories() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        return categoryMapper.selectList(queryWrapper);
    }
    
    @Override
    public Page<Product> getProductsByCategory(Long categoryId, Integer page, Integer size) {
        Page<Product> pageObj = new Page<>(page, size);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId)
                   .eq("status", "active")
                   .orderByDesc("created_at");
        return productMapper.selectPage(pageObj, queryWrapper);
    }
    
    @Override
    public Page<Product> searchProducts(ProductSearchRequest request) {
        Page<Product> pageObj = new Page<>(request.getPage(), request.getSize());
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        
        // 只查询上架商品
        queryWrapper.eq("status", "active");
        
        // 关键词搜索
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                .like("name", request.getKeyword())
                .or()
                .like("description", request.getKeyword())
            );
        }
        
        // 分类筛选
        if (request.getCategoryId() != null) {
            queryWrapper.eq("category_id", request.getCategoryId());
        }
        
        // 价格范围筛选
        if (request.getMinPrice() != null) {
            queryWrapper.ge("price", request.getMinPrice());
        }
        if (request.getMaxPrice() != null) {
            queryWrapper.le("price", request.getMaxPrice());
        }
        
        // 排序
        if (request.getSortBy() != null) {
            switch (request.getSortBy()) {
                case "price_asc":
                    queryWrapper.orderByAsc("price");
                    break;
                case "price_desc":
                    queryWrapper.orderByDesc("price");
                    break;
                case "created_at_desc":
                    queryWrapper.orderByDesc("created_at");
                    break;
                case "sales_desc":
                    // 这里需要关联订单表统计销量，暂时按创建时间排序
                    queryWrapper.orderByDesc("created_at");
                    break;
                default:
                    queryWrapper.orderByDesc("created_at");
            }
        } else {
            queryWrapper.orderByDesc("created_at");
        }
        
        return productMapper.selectPage(pageObj, queryWrapper);
    }
    
    @Override
    public Product getProductById(Long productId) {
        return productMapper.selectById(productId);
    }
    
    @Override
    public List<Product> getHotProducts(Integer limit) {
        return productMapper.getHotProducts(limit != null ? limit : 10);
    }
    
    @Override
    public List<Product> getRecommendedProducts(Long userId, Integer limit) {
        // 简单的推荐逻辑：返回最新上架的商品
        // 实际项目中可以根据用户购买历史、浏览记录等推荐
        Page<Product> pageObj = new Page<>(1, limit != null ? limit : 10);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "active")
                   .orderByDesc("created_at");
        return productMapper.selectPage(pageObj, queryWrapper).getRecords();
    }
}
