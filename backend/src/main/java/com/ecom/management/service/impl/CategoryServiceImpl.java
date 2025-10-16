package com.ecom.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ecom.management.entity.Category;
import com.ecom.management.mapper.CategoryMapper;
import com.ecom.management.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> listAll() {
        QueryWrapper<Category> query = new QueryWrapper<>();
        query.orderByAsc("name");
        return categoryMapper.selectList(query);
    }
}


