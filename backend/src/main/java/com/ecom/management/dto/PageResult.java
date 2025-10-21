package com.ecom.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    
    /** 数据列表 */
    private List<T> records;
    
    /** 总记录数 */
    private long total;
    
    /** 当前页码 */
    private int page;
    
    /** 每页大小 */
    private int size;
}

