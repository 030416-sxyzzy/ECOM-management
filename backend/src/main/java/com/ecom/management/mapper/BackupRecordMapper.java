package com.ecom.management.mapper;

import com.ecom.management.entity.BackupRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 数据库备份记录Mapper接口
 */
@Mapper
public interface BackupRecordMapper {
    
    /**
     * 插入备份记录
     */
    @Insert("INSERT INTO backup_records (backup_file_name, backup_file_path, backup_file_size, backup_type, status, description, created_at) " +
            "VALUES (#{backupFileName}, #{backupFilePath}, #{backupFileSize}, #{backupType}, #{status}, #{description}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(BackupRecord record);
    
    /**
     * 更新备份记录状态
     */
    @Update("UPDATE backup_records SET status = #{status}, backup_file_size = #{backupFileSize} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") String status, @Param("backupFileSize") Long backupFileSize);
    
    /**
     * 查询所有备份记录
     */
    @Select("SELECT * FROM backup_records ORDER BY created_at DESC")
    List<BackupRecord> findAll();
    
    /**
     * 分页查询备份记录
     */
    @Select("SELECT * FROM backup_records ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<BackupRecord> findByPage(@Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * 统计备份记录总数
     */
    @Select("SELECT COUNT(*) FROM backup_records")
    int count();
    
    /**
     * 根据ID查询备份记录
     */
    @Select("SELECT * FROM backup_records WHERE id = #{id}")
    BackupRecord findById(Long id);
    
    /**
     * 删除备份记录
     */
    @Delete("DELETE FROM backup_records WHERE id = #{id}")
    int deleteById(Long id);
}

