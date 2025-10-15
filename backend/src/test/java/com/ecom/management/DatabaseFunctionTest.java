package com.ecom.management;

import com.ecom.management.service.DatabaseBackupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

/**
 * 数据库功能测试类
 */
@SpringBootTest
public class DatabaseFunctionTest {
    
    @Autowired
    private DatabaseBackupService databaseBackupService;
    
    /**
     * 测试商品销售统计视图
     */
    @Test
    public void testProductSalesStats() {
        List<Map<String, Object>> stats = databaseBackupService.getProductSalesStats();
        System.out.println("商品销售统计:");
        stats.forEach(stat -> {
            System.out.println("商品ID: " + stat.get("product_id") + 
                             ", 商品名称: " + stat.get("product_name") + 
                             ", 总销量: " + stat.get("total_sold_quantity") + 
                             ", 总销售额: " + stat.get("total_sales_amount"));
        });
    }
    
    /**
     * 测试用户订单汇总视图
     */
    @Test
    public void testUserOrderSummary() {
        List<Map<String, Object>> summary = databaseBackupService.getUserOrderSummary();
        System.out.println("用户订单汇总:");
        summary.forEach(user -> {
            System.out.println("用户ID: " + user.get("user_id") + 
                             ", 用户名: " + user.get("username") + 
                             ", 总订单数: " + user.get("total_orders") + 
                             ", 总消费: " + user.get("total_spent"));
        });
    }
    
    /**
     * 测试每日销售统计视图
     */
    @Test
    public void testDailySales() {
        List<Map<String, Object>> sales = databaseBackupService.getDailySales();
        System.out.println("每日销售统计:");
        sales.forEach(sale -> {
            System.out.println("销售日期: " + sale.get("sale_date") + 
                             ", 订单数: " + sale.get("order_count") + 
                             ", 客户数: " + sale.get("customer_count") + 
                             ", 总销售额: " + sale.get("total_sales"));
        });
    }
    
    /**
     * 测试销售报表存储过程
     */
    @Test
    public void testSalesReport() {
        // 测试最近30天的销售报表
        java.time.LocalDateTime endDate = java.time.LocalDateTime.now();
        java.time.LocalDateTime startDate = endDate.minusDays(30);
        
        List<Map<String, Object>> report = databaseBackupService.generateSalesReport(startDate, endDate);
        System.out.println("销售报表 (最近30天):");
        report.forEach(day -> {
            System.out.println("日期: " + day.get("report_date") + 
                             ", 订单数: " + day.get("order_count") + 
                             ", 客户数: " + day.get("customer_count") + 
                             ", 总销售额: " + day.get("total_sales") + 
                             ", 平均订单金额: " + day.get("avg_order_value"));
        });
    }
    
    /**
     * 测试数据库备份功能
     */
    @Test
    public void testDatabaseBackup() {
        try {
            // 创建测试备份（定时备份）
            Long backupId = databaseBackupService.createBackup("test_backup");
            System.out.println("备份创建成功，备份ID: " + backupId);
            
            // 获取备份记录
            List<com.ecom.management.entity.BackupRecord> records = databaseBackupService.getBackupRecords();
            System.out.println("备份记录数量: " + records.size());
            
        } catch (Exception e) {
            System.out.println("备份测试失败: " + e.getMessage());
        }
    }
}
