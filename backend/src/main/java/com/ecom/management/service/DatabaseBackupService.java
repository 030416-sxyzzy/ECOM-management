package com.ecom.management.service;

import com.ecom.management.entity.BackupRecord;
import com.ecom.management.entity.InventoryLog;
import com.ecom.management.entity.UserOperationLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据库备份服务接口
 */
public interface DatabaseBackupService {
    
    /**
     * 创建数据库备份（仅限定时备份）
     * @param backupName 备份名称
     * @return 备份记录ID
     */
    Long createBackup(String backupName);
    
    /**
     * 恢复数据库备份
     * @param backupId 备份记录ID
     * @return 是否恢复成功
     */
    boolean restoreBackup(Long backupId);
    
    /**
     * 获取备份记录列表
     * @return 备份记录列表
     */
    List<BackupRecord> getBackupRecords();
    
    /**
     * 删除备份记录
     * @param backupId 备份记录ID
     * @return 是否删除成功
     */
    boolean deleteBackup(Long backupId);
    
    /**
     * 获取库存变更日志
     * @param productId 商品ID（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 库存变更日志列表
     */
    List<InventoryLog> getInventoryLogs(Long productId, Integer page, Integer size);
    
    /**
     * 获取用户操作日志
     * @param userId 用户ID（可选）
     * @param page 页码
     * @param size 每页大小
     * @return 用户操作日志列表
     */
    List<UserOperationLog> getUserOperationLogs(Long userId, Integer page, Integer size);
    
    /**
     * 获取商品销售统计
     * @return 商品销售统计列表
     */
    List<Map<String, Object>> getProductSalesStats();
    
    /**
     * 获取用户订单汇总
     * @return 用户订单汇总列表
     */
    List<Map<String, Object>> getUserOrderSummary();
    
    /**
     * 获取每日销售统计
     * @return 每日销售统计列表
     */
    List<Map<String, Object>> getDailySales();
    
    /**
     * 生成销售报表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 销售报表数据
     */
    List<Map<String, Object>> generateSalesReport(LocalDateTime startDate, LocalDateTime endDate);
}
