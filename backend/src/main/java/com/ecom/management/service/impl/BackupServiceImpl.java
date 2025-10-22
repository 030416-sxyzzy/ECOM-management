package com.ecom.management.service.impl;

import com.ecom.management.entity.BackupRecord;
import com.ecom.management.mapper.BackupRecordMapper;
import com.ecom.management.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 数据库备份服务实现类
 */
@Service
public class BackupServiceImpl implements BackupService {

    @Autowired
    private BackupRecordMapper backupRecordMapper;

    // 从配置文件读取数据库信息
    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/ecom_management}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:root}")
    private String username;

    @Value("${spring.datasource.password:123456}")
    private String password;

    // 备份文件保存目录
    private static final String BACKUP_DIR = "database/backups/";

    @Override
    public Long performBackup(String description) throws Exception {
        // 1. 创建备份记录
        BackupRecord record = new BackupRecord();
        record.setBackupType("manual");
        record.setStatus("in_progress");
        record.setDescription(description != null && !description.isEmpty() ? description : "手动备份");
        
        // 生成备份文件名
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String fileName = "backup_" + timestamp + ".sql";
        String filePath = BACKUP_DIR + fileName;
        
        record.setBackupFileName(fileName);
        record.setBackupFilePath(filePath);
        record.setBackupFileSize(0L);
        
        backupRecordMapper.insert(record);
        
        try {
            // 2. 确保备份目录存在
            File backupDirFile = new File(BACKUP_DIR);
            if (!backupDirFile.exists()) {
                backupDirFile.mkdirs();
            }
            
            // 3. 解析数据库名称
            String dbName = extractDatabaseName(datasourceUrl);
            
            // 4. 构建 mysqldump 命令
            String[] command = {
                "mysqldump",
                "-u" + username,
                "-p" + password,
                "--databases",
                dbName,
                "--result-file=" + filePath,
                "--single-transaction",
                "--quick",
                "--lock-tables=false"
            };
            
            // 5. 执行备份命令
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                // 6. 备份成功，更新记录
                File backupFile = new File(filePath);
                long fileSize = backupFile.exists() ? backupFile.length() : 0;
                
                backupRecordMapper.updateStatus(record.getId(), "success", fileSize);
                
                return record.getId();
            } else {
                // 备份失败
                backupRecordMapper.updateStatus(record.getId(), "failed", 0L);
                throw new Exception("数据库备份失败: " + output.toString());
            }
            
        } catch (Exception e) {
            // 更新状态为失败
            backupRecordMapper.updateStatus(record.getId(), "failed", 0L);
            throw new Exception("数据库备份失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<BackupRecord> getAllBackupRecords() {
        return backupRecordMapper.findAll();
    }

    @Override
    public List<BackupRecord> getBackupRecordsByPage(int page, int size) {
        int offset = (page - 1) * size;
        return backupRecordMapper.findByPage(offset, size);
    }

    @Override
    public int countBackupRecords() {
        return backupRecordMapper.count();
    }

    @Override
    public boolean deleteBackupRecord(Long id) {
        // 1. 查询备份记录
        BackupRecord record = backupRecordMapper.findById(id);
        if (record == null) {
            return false;
        }
        
        // 2. 删除备份文件
        try {
            File backupFile = new File(record.getBackupFilePath());
            if (backupFile.exists()) {
                backupFile.delete();
            }
        } catch (Exception e) {
            // 文件删除失败不影响记录删除
            System.err.println("删除备份文件失败: " + e.getMessage());
        }
        
        // 3. 删除数据库记录
        return backupRecordMapper.deleteById(id) > 0;
    }

    /**
     * 从JDBC URL中提取数据库名称
     */
    private String extractDatabaseName(String jdbcUrl) {
        // jdbc:mysql://localhost:3306/ecom_management?xxx
        // 先找到第一个 ? 的位置
        int questionMark = jdbcUrl.indexOf('?');
        String baseUrl = questionMark > 0 ? jdbcUrl.substring(0, questionMark) : jdbcUrl;
        
        // 在基础URL中找最后一个 /
        int lastSlash = baseUrl.lastIndexOf('/');
        
        if (lastSlash > 0) {
            return baseUrl.substring(lastSlash + 1);
        }
        
        return null;
    }
}

