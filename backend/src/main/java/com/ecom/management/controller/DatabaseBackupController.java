package com.ecom.management.controller;

import com.ecom.management.entity.BackupRecord;
import com.ecom.management.entity.InventoryLog;
import com.ecom.management.entity.UserOperationLog;
import com.ecom.management.service.DatabaseBackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据库备份管理控制器
 */
@RestController
@RequestMapping("/api/backup")
@RequiredArgsConstructor
public class DatabaseBackupController {
    
    private final DatabaseBackupService databaseBackupService;
    
    
    /**
     * 恢复数据库备份
     */
    @PostMapping("/restore/{backupId}")
    public ResponseEntity<Map<String, Object>> restoreBackup(@PathVariable Long backupId) {
        try {
            boolean success = databaseBackupService.restoreBackup(backupId);
            if (success) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "数据库恢复成功"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "数据库恢复失败"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "数据库恢复失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取备份记录列表
     */
    @GetMapping("/records")
    public ResponseEntity<List<BackupRecord>> getBackupRecords() {
        List<BackupRecord> records = databaseBackupService.getBackupRecords();
        return ResponseEntity.ok(records);
    }
    
    /**
     * 删除备份记录
     */
    @DeleteMapping("/records/{backupId}")
    public ResponseEntity<Map<String, Object>> deleteBackup(@PathVariable Long backupId) {
        try {
            boolean success = databaseBackupService.deleteBackup(backupId);
            if (success) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "备份删除成功"
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "备份删除失败"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "备份删除失败: " + e.getMessage()
            ));
        }
    }
    
    /**
     * 获取库存变更日志
     */
    @GetMapping("/inventory-logs")
    public ResponseEntity<List<InventoryLog>> getInventoryLogs(
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        List<InventoryLog> logs = databaseBackupService.getInventoryLogs(productId, page, size);
        return ResponseEntity.ok(logs);
    }
    
    /**
     * 获取用户操作日志
     */
    @GetMapping("/user-operation-logs")
    public ResponseEntity<List<UserOperationLog>> getUserOperationLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        List<UserOperationLog> logs = databaseBackupService.getUserOperationLogs(userId, page, size);
        return ResponseEntity.ok(logs);
    }
    
    /**
     * 获取商品销售统计
     */
    @GetMapping("/stats/product-sales")
    public ResponseEntity<List<Map<String, Object>>> getProductSalesStats() {
        List<Map<String, Object>> stats = databaseBackupService.getProductSalesStats();
        return ResponseEntity.ok(stats);
    }
    
    /**
     * 获取用户订单汇总
     */
    @GetMapping("/stats/user-orders")
    public ResponseEntity<List<Map<String, Object>>> getUserOrderSummary() {
        List<Map<String, Object>> summary = databaseBackupService.getUserOrderSummary();
        return ResponseEntity.ok(summary);
    }
    
    /**
     * 获取每日销售统计
     */
    @GetMapping("/stats/daily-sales")
    public ResponseEntity<List<Map<String, Object>>> getDailySales() {
        List<Map<String, Object>> sales = databaseBackupService.getDailySales();
        return ResponseEntity.ok(sales);
    }
    
    /**
     * 生成销售报表
     */
    @GetMapping("/report/sales")
    public ResponseEntity<List<Map<String, Object>>> generateSalesReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
            List<Map<String, Object>> report = databaseBackupService.generateSalesReport(start, end);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of(Map.of(
                "error", "日期格式错误: " + e.getMessage()
            )));
        }
    }
}
