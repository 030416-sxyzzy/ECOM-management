package com.ecom.management.mapper;

import com.ecom.management.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品Mapper接口
 */
@Mapper
public interface ProductMapper {

    @Select("<script>" +
            "SELECT id, name, description, price, stock, category_id, image_url, status, created_at, updated_at " +
            "FROM products " +
            "WHERE status = 'active' " +
            "<if test='categoryId != null'> AND category_id = #{categoryId} </if>" +
            "<if test='keyword != null and keyword != \"\"'> AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')) </if>" +
            "<if test='minPrice != null'> AND price &gt;= #{minPrice} </if>" +
            "<if test='maxPrice != null'> AND price &lt;= #{maxPrice} </if>" +
            "ORDER BY ${sortBy} ${sortOrder} " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Product> searchProducts(@Param("categoryId") Long categoryId,
                                 @Param("keyword") String keyword,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice,
                                 @Param("sortBy") String sortBy,
                                 @Param("sortOrder") String sortOrder,
                                 @Param("offset") int offset,
                                 @Param("pageSize") int pageSize);

    @Select("<script>" +
            "SELECT COUNT(id) FROM products " +
            "WHERE status = 'active' " +
            "<if test='categoryId != null'> AND category_id = #{categoryId} </if>" +
            "<if test='keyword != null and keyword != \"\"'> AND (name LIKE CONCAT('%', #{keyword}, '%') OR description LIKE CONCAT('%', #{keyword}, '%')) </if>" +
            "<if test='minPrice != null'> AND price &gt;= #{minPrice} </if>" +
            "<if test='maxPrice != null'> AND price &lt;= #{maxPrice} </if>" +
            "</script>")
    int countSearchProducts(@Param("categoryId") Long categoryId,
                           @Param("keyword") String keyword,
                           @Param("minPrice") BigDecimal minPrice,
                           @Param("maxPrice") BigDecimal maxPrice);

    @Select("SELECT id, name, description, price, stock, category_id, image_url, status, created_at, updated_at FROM products WHERE id = #{id}")
    Product findById(Long id);

    @Select("SELECT id, name, description, price, stock, category_id, image_url, status, created_at, updated_at FROM products WHERE status = 'active' ORDER BY created_at DESC LIMIT #{limit}")
    List<Product> findHotProducts(int limit);

    @Select("SELECT id, name, description, price, stock, category_id, image_url, status, created_at, updated_at FROM products WHERE status = 'active' ORDER BY RAND() LIMIT #{limit}")
    List<Product> findRecommendedProducts(int limit);
}
