package com.ecom.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecom.management.entity.BackupRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 备份记录Mapper接口
 */
@Mapper
public interface BackupRecordMapper extends BaseMapper<BackupRecord> {
}
