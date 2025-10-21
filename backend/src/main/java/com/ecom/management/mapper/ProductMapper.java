package com.ecom.management.mapper;

import com.ecom.management.entity.Product;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品Mapper接口
 */
@Mapper
public interface ProductMapper {

    @Insert("INSERT INTO products (name, description, price, stock, category_id, image_url, status, created_at, updated_at) " +
            "VALUES (#{name}, #{description}, #{price}, #{stock}, #{categoryId}, #{imageUrl}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Update("UPDATE products SET name=#{name}, description=#{description}, price=#{price}, stock=#{stock}, " +
            "category_id=#{categoryId}, image_url=#{imageUrl}, status=#{status}, updated_at=NOW() WHERE id=#{id}")
    int update(Product product);

    @Delete("DELETE FROM products WHERE id=#{id}")
    int deleteById(Long id);

    @Select("SELECT id, name, description, price, stock, category_id, image_url, status, created_at, updated_at FROM products WHERE id = #{id}")
    Product findById(Long id);

    @Select("<script>" +
            "SELECT id, name, description, price, stock, category_id, image_url, status, created_at, updated_at " +
            "FROM products WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'> AND name LIKE CONCAT('%', #{keyword}, '%') </if>" +
            "<if test='categoryId != null'> AND category_id = #{categoryId} </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "ORDER BY updated_at DESC " +
            "LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<Product> findByPage(@Param("keyword") String keyword,
                             @Param("categoryId") Long categoryId,
                             @Param("status") String status,
                             @Param("offset") int offset,
                             @Param("pageSize") int pageSize);

    @Select("<script>" +
            "SELECT COUNT(id) FROM products WHERE 1=1 " +
            "<if test='keyword != null and keyword != \"\"'> AND name LIKE CONCAT('%', #{keyword}, '%') </if>" +
            "<if test='categoryId != null'> AND category_id = #{categoryId} </if>" +
            "<if test='status != null and status != \"\"'> AND status = #{status} </if>" +
            "</script>")
    int countByPage(@Param("keyword") String keyword,
                    @Param("categoryId") Long categoryId,
                    @Param("status") String status);

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

    @Select("SELECT id, name, description, price, stock, category_id, image_url, status, created_at, updated_at FROM products WHERE status = 'active' ORDER BY created_at DESC LIMIT #{limit}")
    List<Product> findHotProducts(int limit);

    @Select("SELECT id, name, description, price, stock, category_id, image_url, status, created_at, updated_at FROM products WHERE status = 'active' ORDER BY RAND() LIMIT #{limit}")
    List<Product> findRecommendedProducts(int limit);

    @Select("<script>" +
            "SELECT id, name, description, price, stock, category_id, image_url, status, created_at, updated_at " +
            "FROM products WHERE status = 'active' " +
            "<if test='categoryId != null'> AND category_id = #{categoryId} </if>" +
            "ORDER BY updated_at DESC" +
            "</script>")
    List<Product> findByCategory(@Param("categoryId") Long categoryId);
}
