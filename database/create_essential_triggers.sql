USE ecom_management;

-- ===========================================
-- 1. 创建必需的日志表
-- ===========================================

-- 库存变更日志表
CREATE TABLE IF NOT EXISTS inventory_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL COMMENT '商品ID',
    change_type ENUM('in', 'out') NOT NULL COMMENT '变更类型：入库/出库',
    quantity INT NOT NULL COMMENT '变更数量',
    reason VARCHAR(500) NOT NULL COMMENT '变更原因',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_product_id (product_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存变更日志表';

-- 备份记录表（简化版）
CREATE TABLE IF NOT EXISTS backup_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    backup_name VARCHAR(255) NOT NULL COMMENT '备份名称',
    backup_path VARCHAR(500) NOT NULL COMMENT '备份文件路径',
    file_size BIGINT DEFAULT 0 COMMENT '文件大小（字节）',
    status ENUM('success', 'failed') DEFAULT 'success' COMMENT '备份状态',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据库备份记录表';

-- ===========================================
-- 2. 创建触发器
-- ===========================================

-- 删除已存在的触发器
DROP TRIGGER IF EXISTS tr_order_items_after_insert;
DROP TRIGGER IF EXISTS tr_orders_after_update;

-- 订单创建时自动扣减库存
DELIMITER $$
CREATE TRIGGER tr_order_items_after_insert
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    UPDATE products 
    SET stock = stock - NEW.quantity,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.product_id;
    
    INSERT INTO inventory_logs (product_id, change_type, quantity, reason)
    VALUES (NEW.product_id, 'out', NEW.quantity, CONCAT('订单扣减，订单ID：', NEW.order_id));
END$$
DELIMITER ;

-- 订单取消时恢复库存
DELIMITER $$
CREATE TRIGGER tr_orders_after_update
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    IF OLD.status != 'cancelled' AND NEW.status = 'cancelled' THEN
        UPDATE products p
        INNER JOIN order_items oi ON p.id = oi.product_id
        SET p.stock = p.stock + oi.quantity,
            p.updated_at = CURRENT_TIMESTAMP
        WHERE oi.order_id = NEW.id;
        
        INSERT INTO inventory_logs (product_id, change_type, quantity, reason)
        SELECT oi.product_id, 'in', oi.quantity, CONCAT('订单取消恢复，订单ID：', NEW.id)
        FROM order_items oi
        WHERE oi.order_id = NEW.id;
    END IF;
END$$
DELIMITER ;

-- ===========================================
-- 3. 创建视图
-- ===========================================

-- 删除已存在的视图
DROP VIEW IF EXISTS v_product_sales_stats;
DROP VIEW IF EXISTS v_daily_sales;

-- 商品销售统计视图
CREATE VIEW v_product_sales_stats AS
SELECT 
    p.id as product_id,
    p.name as product_name,
    p.price as current_price,
    COALESCE(SUM(oi.quantity), 0) as total_sold_quantity,
    COALESCE(SUM(oi.quantity * oi.price), 0) as total_sales_amount,
    COALESCE(COUNT(DISTINCT oi.order_id), 0) as order_count,
    p.stock as current_stock,
    CASE 
        WHEN p.stock = 0 THEN '缺货'
        WHEN p.stock < 10 THEN '库存不足'
        ELSE '库存充足'
    END as stock_status
FROM products p
LEFT JOIN order_items oi ON p.id = oi.product_id
LEFT JOIN orders o ON oi.order_id = o.id AND o.status != 'cancelled'
GROUP BY p.id, p.name, p.price, p.stock;


-- 每日销售统计视图
CREATE VIEW v_daily_sales AS
SELECT 
    DATE(o.created_at) as sale_date,
    COUNT(DISTINCT o.id) as order_count,
    COUNT(DISTINCT o.user_id) as customer_count,
    SUM(o.total_amount) as total_sales,
    AVG(o.total_amount) as avg_order_value,
    COUNT(DISTINCT oi.product_id) as product_variety
FROM orders o
LEFT JOIN order_items oi ON o.id = oi.order_id
WHERE o.status != 'cancelled'
GROUP BY DATE(o.created_at)
ORDER BY sale_date DESC;

-- ===========================================
-- 4. 显示创建结果
-- ===========================================

SELECT '✅ 数据库功能创建完成！' AS message;
SELECT '已创建的功能：' AS info UNION ALL
SELECT '  - 库存变更日志表 (inventory_logs)' UNION ALL
SELECT '  - 备份记录表 (backup_records)' UNION ALL
SELECT '  - 订单库存扣减触发器 (tr_order_items_after_insert)' UNION ALL
SELECT '  - 订单取消恢复触发器 (tr_orders_after_update)' UNION ALL
SELECT '  - 商品销售统计视图 (v_product_sales_stats)' UNION ALL
SELECT '  - 每日销售统计视图 (v_daily_sales)';
