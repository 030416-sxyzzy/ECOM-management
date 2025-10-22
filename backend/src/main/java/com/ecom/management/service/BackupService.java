package com.ecom.management.service;

import com.ecom.management.entity.BackupRecord;

import java.util.List;

/**
 * 数据库备份服务接口
 */
public interface BackupService {
    
    /**
     * 执行数据库备份
     * @param description 备份说明
     * @return 备份记录ID
     */
    Long performBackup(String description) throws Exception;
    
    /**
     * 查询所有备份记录
     */
    List<BackupRecord> getAllBackupRecords();
    
    /**
     * 分页查询备份记录
     */
    List<BackupRecord> getBackupRecordsByPage(int page, int size);
    
    /**
     * 统计备份记录总数
     */
    int countBackupRecords();
    
    /**
     * 删除备份记录
     */
    boolean deleteBackupRecord(Long id);
}

