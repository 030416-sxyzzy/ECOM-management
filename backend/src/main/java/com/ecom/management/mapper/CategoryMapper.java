package com.ecom.management.mapper;

import com.ecom.management.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品分类Mapper
 */
@Mapper
public interface CategoryMapper {
    
    @Select("SELECT * FROM categories ORDER BY name")
    List<Category> findAll();
    
    @Select("SELECT * FROM categories WHERE id = #{id}")
    Category findById(Long id);
}
