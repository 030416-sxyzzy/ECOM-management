-- 更新 backup_records 表结构以支持新的备份功能

USE ecom_management;

-- 1. 删除旧表（如果需要保留数据，请跳过此步骤）
DROP TABLE IF EXISTS backup_records;

-- 2. 创建新的 backup_records 表
CREATE TABLE IF NOT EXISTS backup_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '备份记录ID',
    backup_file_name VARCHAR(255) NOT NULL COMMENT '备份文件名',
    backup_file_path VARCHAR(500) NOT NULL COMMENT '备份文件路径',
    backup_file_size BIGINT DEFAULT 0 COMMENT '备份文件大小（字节）',
    backup_type VARCHAR(20) DEFAULT 'manual' COMMENT '备份类型：manual-手动, auto-自动',
    status VARCHAR(20) DEFAULT 'success' COMMENT '备份状态：success-成功, failed-失败, in_progress-进行中',
    description VARCHAR(500) COMMENT '备份说明',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_created_at (created_at),
    INDEX idx_status (status),
    INDEX idx_backup_type (backup_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据库备份记录表';

SELECT '✅ backup_records 表结构已更新' AS message;

