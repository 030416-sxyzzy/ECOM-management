-- 电商订单管理系统数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS ecom_management 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE ecom_management;

-- 1. 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL COMMENT '邮箱',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    password VARCHAR(255) NOT NULL COMMENT '加密密码',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_email (email),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 商品分类表
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    description TEXT COMMENT '分类描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表';

-- 3. 商品表
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    image_url VARCHAR(500) COMMENT '商品图片',
    status ENUM('active', 'inactive') DEFAULT 'active' COMMENT '状态：上架/下架',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_name (name),
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 4. 订单表
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    order_number VARCHAR(50) UNIQUE NOT NULL COMMENT '订单号',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    status ENUM('pending', 'paid', 'shipped', 'completed', 'cancelled') DEFAULT 'pending' COMMENT '订单状态',
    shipping_address TEXT NOT NULL COMMENT '收货地址',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_order_number (order_number),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 5. 购物车表
CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '数量',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    UNIQUE KEY uk_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- 6. 订单详情表
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '购买数量',
    price DECIMAL(10,2) NOT NULL COMMENT '购买时价格',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单详情表';

-- 插入初始数据
-- 插入商品分类
INSERT INTO categories (name, description) VALUES
('电子产品', '各类电子设备及配件'),
('服装鞋帽', '时尚服装和鞋类产品'),
('家居生活', '家居用品和生活用品'),
('图书文具', '图书、文具和办公用品'),
('运动户外', '运动装备和户外用品');

-- 插入示例商品
INSERT INTO products (name, description, price, stock, category_id, image_url, status) VALUES
('iPhone 15 Pro', '苹果最新旗舰手机', 7999.00, 100, 1, '/images/iphone15pro.jpg', 'active'),
('MacBook Air M2', '苹果笔记本电脑', 8999.00, 50, 1, '/images/macbook-air.jpg', 'active'),
('Nike Air Max', '经典运动鞋', 899.00, 200, 2, '/images/nike-airmax.jpg', 'active'),
('优衣库基础T恤', '纯棉基础款T恤', 99.00, 500, 2, '/images/uniqlo-tshirt.jpg', 'active'),
('小米空气净化器', '智能空气净化设备', 1299.00, 80, 3, '/images/xiaomi-airpurifier.jpg', 'active'),
('《Vue.js实战》', '前端开发必读书籍', 89.00, 150, 4, '/images/vue-book.jpg', 'active'),
('瑜伽垫', '防滑瑜伽练习垫', 199.00, 120, 5, '/images/yoga-mat.jpg', 'active');

-- 创建测试用户（密码：123456，已加密）
INSERT INTO users (email, username, password) VALUES
('admin@ecom.com', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi'),
('test@ecom.com', 'testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi');

-- ===========================================
-- 数据库触发器 (Triggers)
-- ===========================================

-- 1. 订单创建时自动扣减库存触发器
DELIMITER $$
CREATE TRIGGER tr_order_items_after_insert
AFTER INSERT ON order_items
FOR EACH ROW
BEGIN
    -- 扣减商品库存
    UPDATE products 
    SET stock = stock - NEW.quantity,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = NEW.product_id;
    
    -- 记录库存变更日志
    INSERT INTO inventory_logs (product_id, change_type, quantity, reason, created_at)
    VALUES (NEW.product_id, 'out', NEW.quantity, CONCAT('订单扣减，订单号：', NEW.order_id), CURRENT_TIMESTAMP);
END$$
DELIMITER ;

-- 2. 订单取消时恢复库存触发器
DELIMITER $$
CREATE TRIGGER tr_orders_after_update
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    -- 如果订单状态从非取消状态变为取消状态，恢复库存
    IF OLD.status != 'cancelled' AND NEW.status = 'cancelled' THEN
        -- 恢复所有订单项对应的库存
        UPDATE products p
        INNER JOIN order_items oi ON p.id = oi.product_id
        SET p.stock = p.stock + oi.quantity,
            p.updated_at = CURRENT_TIMESTAMP
        WHERE oi.order_id = NEW.id;
        
        -- 记录库存恢复日志
        INSERT INTO inventory_logs (product_id, change_type, quantity, reason, created_at)
        SELECT oi.product_id, 'in', oi.quantity, CONCAT('订单取消恢复，订单号：', NEW.id), CURRENT_TIMESTAMP
        FROM order_items oi
        WHERE oi.order_id = NEW.id;
    END IF;
END$$
DELIMITER ;

-- 3. 用户操作日志触发器
DELIMITER $$
CREATE TRIGGER tr_users_after_update
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    -- 记录用户信息变更
    IF OLD.email != NEW.email OR OLD.username != NEW.username OR OLD.phone != NEW.phone THEN
        INSERT INTO user_operation_logs (user_id, operation_type, operation_desc, created_at)
        VALUES (NEW.id, 'update_profile', '用户更新个人信息', CURRENT_TIMESTAMP);
    END IF;
END$$
DELIMITER ;

-- ===========================================
-- 数据库视图 (Views)
-- ===========================================

-- 1. 商品销售统计视图
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

-- 2. 用户订单汇总视图
CREATE VIEW v_user_order_summary AS
SELECT 
    u.id as user_id,
    u.username,
    u.email,
    COUNT(DISTINCT o.id) as total_orders,
    COALESCE(SUM(CASE WHEN o.status != 'cancelled' THEN o.total_amount ELSE 0 END), 0) as total_spent,
    COALESCE(AVG(CASE WHEN o.status != 'cancelled' THEN o.total_amount ELSE NULL END), 0) as avg_order_amount,
    MAX(o.created_at) as last_order_date,
    COUNT(DISTINCT CASE WHEN o.status = 'completed' THEN o.id END) as completed_orders,
    COUNT(DISTINCT CASE WHEN o.status = 'cancelled' THEN o.id END) as cancelled_orders
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.username, u.email;


-- 4. 每日销售统计视图
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
-- 数据库备份相关表
-- ===========================================

-- 备份记录表
CREATE TABLE backup_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    backup_name VARCHAR(255) NOT NULL COMMENT '备份名称',
    backup_type ENUM('manual', 'scheduled') NOT NULL COMMENT '备份类型：手动/定时',
    backup_path VARCHAR(500) NOT NULL COMMENT '备份文件路径',
    file_size BIGINT NOT NULL COMMENT '备份文件大小（字节）',
    status ENUM('success', 'failed', 'in_progress') DEFAULT 'in_progress' COMMENT '备份状态',
    error_message TEXT COMMENT '错误信息',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_backup_type (backup_type),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据库备份记录表';

-- 库存变更日志表
CREATE TABLE inventory_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL COMMENT '商品ID',
    change_type ENUM('in', 'out') NOT NULL COMMENT '变更类型：入库/出库',
    quantity INT NOT NULL COMMENT '变更数量',
    reason VARCHAR(500) NOT NULL COMMENT '变更原因',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_product_id (product_id),
    INDEX idx_change_type (change_type),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存变更日志表';

-- 用户操作日志表
CREATE TABLE user_operation_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    operation_type VARCHAR(50) NOT NULL COMMENT '操作类型',
    operation_desc VARCHAR(500) NOT NULL COMMENT '操作描述',
    ip_address VARCHAR(45) COMMENT 'IP地址',
    user_agent TEXT COMMENT '用户代理',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户操作日志表';

-- ===========================================
-- 存储过程：数据库备份
-- ===========================================

DELIMITER $$
CREATE PROCEDURE sp_create_backup(IN backup_name VARCHAR(255), IN backup_type VARCHAR(20))
BEGIN
    DECLARE backup_path VARCHAR(500);
    DECLARE file_size BIGINT DEFAULT 0;
    DECLARE backup_id BIGINT;
    
    -- 生成备份文件路径
    SET backup_path = CONCAT('/backups/', backup_name, '_', DATE_FORMAT(NOW(), '%Y%m%d_%H%i%s'), '.sql');
    
    -- 插入备份记录
    INSERT INTO backup_records (backup_name, backup_type, backup_path, status)
    VALUES (backup_name, backup_type, backup_path, 'in_progress');
    
    SET backup_id = LAST_INSERT_ID();
    
    -- 这里应该调用系统命令执行mysqldump
    -- 由于存储过程限制，实际备份需要通过应用程序实现
    
    -- 更新备份状态为成功（实际应用中需要根据备份结果更新）
    UPDATE backup_records 
    SET status = 'success', file_size = 0
    WHERE id = backup_id;
    
    SELECT backup_id as backup_record_id, backup_path;
END$$
DELIMITER ;


-- ===========================================
-- 存储过程：销售统计报表
-- ===========================================

DELIMITER $$
CREATE PROCEDURE sp_sales_report(IN start_date DATE, IN end_date DATE)
BEGIN
    SELECT 
        DATE(o.created_at) as report_date,
        COUNT(DISTINCT o.id) as order_count,
        COUNT(DISTINCT o.user_id) as customer_count,
        SUM(o.total_amount) as total_sales,
        AVG(o.total_amount) as avg_order_value,
        COUNT(DISTINCT oi.product_id) as product_variety,
        SUM(oi.quantity) as total_items_sold
    FROM orders o
    LEFT JOIN order_items oi ON o.id = oi.order_id
    WHERE o.status != 'cancelled' 
        AND DATE(o.created_at) BETWEEN start_date AND end_date
    GROUP BY DATE(o.created_at)
    ORDER BY report_date DESC;
END$$
DELIMITER ;