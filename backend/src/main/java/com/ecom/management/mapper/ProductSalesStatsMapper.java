package com.ecom.management.mapper;

import com.ecom.management.entity.ProductSalesStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品销量统计Mapper接口
 */
@Mapper
public interface ProductSalesStatsMapper {
    
    /**
     * 查询所有商品销量统计
     * @return 商品销量统计列表
     */
    @Select("SELECT * FROM v_product_sales_stats ORDER BY total_sold_quantity DESC")
    List<ProductSalesStats> findAllSalesStats();
    
    /**
     * 查询商品销量统计（分页）
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 商品销量统计列表
     */
    @Select("SELECT * FROM v_product_sales_stats ORDER BY total_sold_quantity DESC LIMIT #{offset}, #{limit}")
    List<ProductSalesStats> findSalesStatsByPage(int offset, int limit);
    
    /**
     * 统计总数
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM v_product_sales_stats")
    int countSalesStats();
}

