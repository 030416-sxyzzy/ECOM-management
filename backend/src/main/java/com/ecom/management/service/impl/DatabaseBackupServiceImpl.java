package com.ecom.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ecom.management.entity.BackupRecord;
import com.ecom.management.entity.InventoryLog;
import com.ecom.management.entity.UserOperationLog;
import com.ecom.management.mapper.BackupRecordMapper;
import com.ecom.management.mapper.InventoryLogMapper;
import com.ecom.management.mapper.UserOperationLogMapper;
import com.ecom.management.service.DatabaseBackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 数据库备份服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseBackupServiceImpl implements DatabaseBackupService {
    
    private final BackupRecordMapper backupRecordMapper;
    private final InventoryLogMapper inventoryLogMapper;
    private final UserOperationLogMapper userOperationLogMapper;
    private final JdbcTemplate jdbcTemplate;
    
    @Value("${spring.datasource.url}")
    private String databaseUrl;
    
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    
    @Value("${spring.datasource.password}")
    private String databasePassword;
    
    @Value("${app.backup.path:/backups}")
    private String backupPath;
    
    @Override
    public Long createBackup(String backupName) {
        try {
            // 创建备份记录（仅限定时备份）
            BackupRecord backupRecord = new BackupRecord();
            backupRecord.setBackupName(backupName);
            backupRecord.setBackupType("scheduled");
            backupRecord.setStatus("in_progress");
            backupRecord.setCreatedAt(LocalDateTime.now());
            
            // 生成备份文件路径
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = backupName + "_" + timestamp + ".sql";
            String fullPath = backupPath + "/" + fileName;
            backupRecord.setBackupPath(fullPath);
            
            // 保存备份记录
            backupRecordMapper.insert(backupRecord);
            Long backupId = backupRecord.getId();
            
            // 执行数据库备份
            boolean success = executeBackup(fullPath);
            
            if (success) {
                // 更新备份状态为成功
                backupRecord.setStatus("success");
                File backupFile = new File(fullPath);
                if (backupFile.exists()) {
                    backupRecord.setFileSize(backupFile.length());
                }
            } else {
                // 更新备份状态为失败
                backupRecord.setStatus("failed");
                backupRecord.setErrorMessage("备份执行失败");
            }
            
            backupRecordMapper.updateById(backupRecord);
            return backupId;
            
        } catch (Exception e) {
            log.error("创建数据库备份失败", e);
            throw new RuntimeException("创建数据库备份失败: " + e.getMessage());
        }
    }
    
    @Override
    public boolean restoreBackup(Long backupId) {
        try {
            // 获取备份记录
            BackupRecord backupRecord = backupRecordMapper.selectById(backupId);
            if (backupRecord == null || !"success".equals(backupRecord.getStatus())) {
                throw new RuntimeException("备份记录不存在或备份状态不正确");
            }
            
            // 执行数据库恢复
            return executeRestore(backupRecord.getBackupPath());
            
        } catch (Exception e) {
            log.error("恢复数据库备份失败", e);
            throw new RuntimeException("恢复数据库备份失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<BackupRecord> getBackupRecords() {
        QueryWrapper<BackupRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("created_at");
        return backupRecordMapper.selectList(queryWrapper);
    }
    
    @Override
    public boolean deleteBackup(Long backupId) {
        try {
            // 获取备份记录
            BackupRecord backupRecord = backupRecordMapper.selectById(backupId);
            if (backupRecord != null) {
                // 删除备份文件
                File backupFile = new File(backupRecord.getBackupPath());
                if (backupFile.exists()) {
                    backupFile.delete();
                }
                
                // 删除数据库记录
                backupRecordMapper.deleteById(backupId);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("删除备份失败", e);
            return false;
        }
    }
    
    @Override
    public List<InventoryLog> getInventoryLogs(Long productId, Integer page, Integer size) {
        QueryWrapper<InventoryLog> queryWrapper = new QueryWrapper<>();
        if (productId != null) {
            queryWrapper.eq("product_id", productId);
        }
        queryWrapper.orderByDesc("created_at");
        
        Page<InventoryLog> pageObj = new Page<>(page, size);
        return inventoryLogMapper.selectPage(pageObj, queryWrapper).getRecords();
    }
    
    @Override
    public List<UserOperationLog> getUserOperationLogs(Long userId, Integer page, Integer size) {
        QueryWrapper<UserOperationLog> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        queryWrapper.orderByDesc("created_at");
        
        Page<UserOperationLog> pageObj = new Page<>(page, size);
        return userOperationLogMapper.selectPage(pageObj, queryWrapper).getRecords();
    }
    
    @Override
    public List<Map<String, Object>> getProductSalesStats() {
        String sql = "SELECT * FROM v_product_sales_stats ORDER BY total_sales_amount DESC";
        return jdbcTemplate.queryForList(sql);
    }
    
    @Override
    public List<Map<String, Object>> getUserOrderSummary() {
        String sql = "SELECT * FROM v_user_order_summary ORDER BY total_spent DESC";
        return jdbcTemplate.queryForList(sql);
    }
    
    @Override
    public List<Map<String, Object>> getDailySales() {
        String sql = "SELECT * FROM v_daily_sales LIMIT 30";
        return jdbcTemplate.queryForList(sql);
    }
    
    @Override
    public List<Map<String, Object>> generateSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "CALL sp_sales_report(?, ?)";
        return jdbcTemplate.queryForList(sql, startDate.toLocalDate(), endDate.toLocalDate());
    }
    
    /**
     * 执行数据库备份
     */
    private boolean executeBackup(String backupPath) {
        try {
            // 确保备份目录存在
            Path path = Paths.get(backupPath);
            Files.createDirectories(path.getParent());
            
            // 构建mysqldump命令
            String databaseName = extractDatabaseName(databaseUrl);
            String command = String.format(
                "mysqldump -h%s -u%s -p%s --single-transaction --routines --triggers %s > %s",
                extractHost(databaseUrl),
                databaseUsername,
                databasePassword,
                databaseName,
                backupPath
            );
            
            // 执行备份命令
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            
            return exitCode == 0;
            
        } catch (Exception e) {
            log.error("执行数据库备份命令失败", e);
            return false;
        }
    }
    
    /**
     * 执行数据库恢复
     */
    private boolean executeRestore(String backupPath) {
        try {
            File backupFile = new File(backupPath);
            if (!backupFile.exists()) {
                throw new RuntimeException("备份文件不存在: " + backupPath);
            }
            
            // 构建mysql恢复命令
            String databaseName = extractDatabaseName(databaseUrl);
            String command = String.format(
                "mysql -h%s -u%s -p%s %s < %s",
                extractHost(databaseUrl),
                databaseUsername,
                databasePassword,
                databaseName,
                backupPath
            );
            
            // 执行恢复命令
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            
            return exitCode == 0;
            
        } catch (Exception e) {
            log.error("执行数据库恢复命令失败", e);
            return false;
        }
    }
    
    /**
     * 从数据库URL中提取主机名
     */
    private String extractHost(String url) {
        // jdbc:mysql://localhost:3306/database_name
        String[] parts = url.split("//")[1].split("/")[0];
        return parts.split(":")[0];
    }
    
    /**
     * 从数据库URL中提取数据库名
     */
    private String extractDatabaseName(String url) {
        // jdbc:mysql://localhost:3306/database_name
        String[] parts = url.split("/");
        String lastPart = parts[parts.length - 1];
        String[] dbParts = lastPart.split("\\?");
        return dbParts[0];
    }
}
