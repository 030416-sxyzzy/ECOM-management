package com.ecom.management.enums;

import lombok.Getter;

/**
 * 订单状态枚举类
 */
@Getter
public enum OrderStatus {
    PENDING("pending", "待付款"),
    PAID("paid", "已付款"),
    SHIPPED("shipped", "已发货"),
    COMPLETED("completed", "已完成"),
    CANCELLED("cancelled", "已取消");

    private final String code;
    private final String description;

    OrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // 根据编码获取枚举
    public static OrderStatus getByCode(String code) {
        for (OrderStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}