package com.ecom.management.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 数据库备份记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("backup_records")
public class BackupRecord {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 备份名称
     */
    private String backupName;
    
    /**
     * 备份类型：manual-手动, scheduled-定时
     */
    private String backupType;
    
    /**
     * 备份文件路径
     */
    private String backupPath;
    
    /**
     * 备份文件大小（字节）
     */
    private Long fileSize;
    
    /**
     * 备份状态：success-成功, failed-失败, in_progress-进行中
     */
    private String status;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
