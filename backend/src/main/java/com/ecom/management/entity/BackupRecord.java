package com.ecom.management.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 数据库备份记录实体类
 */
@Data
public class BackupRecord {
    /** 备份ID */
    private Long id;
    
    /** 备份文件名 */
    private String backupFileName;
    
    /** 备份文件路径 */
    private String backupFilePath;
    
    /** 备份文件大小（字节） */
    private Long backupFileSize;
    
    /** 备份类型（manual:手动, auto:自动） */
    private String backupType;
    
    /** 备份状态（success:成功, failed:失败, in_progress:进行中） */
    private String status;
    
    /** 备份说明 */
    private String description;
    
    /** 创建时间 */
    private LocalDateTime createdAt;
}

