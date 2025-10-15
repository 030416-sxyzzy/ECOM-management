package com.ecom.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecom.management.entity.InventoryLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 库存变更日志Mapper接口
 */
@Mapper
public interface InventoryLogMapper extends BaseMapper<InventoryLog> {
}
