# 电商管理系统数据库功能使用指南

## 🎯 功能概述

本系统集成了数据库应用技术课程的核心功能：
- **触发器 (Triggers)**: 自动库存管理、操作日志记录
- **视图 (Views)**: 数据统计、报表展示
- **数据库备份**: 定时备份、数据恢复

## 🚀 快速开始

### 1. 数据库初始化

```bash
# 执行数据库初始化脚本
mysql -u root -p < database/init.sql
```

### 2. 启动应用

```bash
# 启动Spring Boot应用
cd backend
mvn spring-boot:run
```

### 3. 测试API接口

```bash
# 查看备份记录
curl -X GET "http://localhost:8080/api/backup/records"

# 查看商品销售统计
curl -X GET "http://localhost:8080/api/backup/stats/product-sales"
```

## 📊 数据库视图功能

### 1. 商品销售统计视图

**视图名称**: `v_product_sales_stats`

**功能**: 统计每个商品的销售情况

**查询示例**:
```sql
-- 查看所有商品销售统计
SELECT * FROM v_product_sales_stats ORDER BY total_sales_amount DESC;

-- 查看库存不足的商品
SELECT * FROM v_product_sales_stats WHERE stock_status = '库存不足';
```

**API接口**:
```http
GET /api/backup/stats/product-sales
```

### 2. 用户订单汇总视图

**视图名称**: `v_user_order_summary`

**功能**: 统计每个用户的订单情况

**查询示例**:
```sql
-- 查看用户消费排行
SELECT * FROM v_user_order_summary ORDER BY total_spent DESC LIMIT 10;

-- 查看活跃用户
SELECT * FROM v_user_order_summary WHERE total_orders > 5;
```

**API接口**:
```http
GET /api/backup/stats/user-orders
```

### 3. 每日销售统计视图

**视图名称**: `v_daily_sales`

**功能**: 按日期统计销售数据

**查询示例**:
```sql
-- 查看最近7天的销售情况
SELECT * FROM v_daily_sales ORDER BY sale_date DESC LIMIT 7;

-- 查看销售额最高的日期
SELECT * FROM v_daily_sales ORDER BY total_sales DESC LIMIT 1;
```

**API接口**:
```http
GET /api/backup/stats/daily-sales
```

## ⚡ 触发器功能

### 1. 库存自动扣减触发器

**触发器名称**: `tr_order_items_after_insert`

**触发时机**: 订单详情插入时

**功能**: 自动扣减商品库存并记录日志

**测试方法**:
```sql
-- 插入订单详情，触发库存扣减
INSERT INTO order_items (order_id, product_id, quantity, price) 
VALUES (1, 1, 2, 7999.00);

-- 查看库存变更日志
SELECT * FROM inventory_logs ORDER BY created_at DESC LIMIT 5;
```

### 2. 订单取消库存恢复触发器

**触发器名称**: `tr_orders_after_update`

**触发时机**: 订单状态更新时

**功能**: 订单取消时自动恢复库存

**测试方法**:
```sql
-- 取消订单，触发库存恢复
UPDATE orders SET status = 'cancelled' WHERE id = 1;

-- 查看库存恢复日志
SELECT * FROM inventory_logs WHERE change_type = 'in' ORDER BY created_at DESC;
```

### 3. 用户操作日志触发器

**触发器名称**: `tr_users_after_update`

**触发时机**: 用户信息更新时

**功能**: 自动记录用户操作日志

**测试方法**:
```sql
-- 更新用户信息，触发操作日志
UPDATE users SET username = 'new_username' WHERE id = 1;

-- 查看用户操作日志
SELECT * FROM user_operation_logs ORDER BY created_at DESC LIMIT 5;
```

## 💾 数据库备份功能

### 1. 定时备份

系统自动执行以下定时备份：
- **每日备份**: 凌晨2点
- **每周备份**: 周日凌晨3点  
- **每月备份**: 每月1号凌晨4点

### 3. 备份恢复

**API接口**:
```http
POST /api/backup/restore/{backupId}
```

**响应示例**:
```json
{
  "success": true,
  "message": "数据库恢复成功"
}
```

### 4. 备份管理

**查看备份记录**:
```http
GET /api/backup/records
```

**删除备份**:
```http
DELETE /api/backup/records/{backupId}
```

### 5. 脚本备份

**执行备份**:
```bash
# 给脚本执行权限
chmod +x scripts/backup_database.sh

# 执行备份
./scripts/backup_database.sh ecom_management my_backup.sql
```

**恢复备份**:
```bash
# 给脚本执行权限
chmod +x scripts/restore_database.sh

# 恢复备份
./scripts/restore_database.sh ecom_management /backups/my_backup.sql
```

## 📈 存储过程功能

### 1. 销售统计报表

**存储过程**: `sp_sales_report`

**参数**: `start_date`, `end_date` - 日期范围

**调用示例**:
```sql
-- 生成2024年1月的销售报表
CALL sp_sales_report('2024-01-01', '2024-01-31');
```

**API接口**:
```http
GET /api/backup/report/sales?startDate=2024-01-01&endDate=2024-01-31
```

## 🔍 日志管理

### 1. 库存变更日志

**表名**: `inventory_logs`

**查看日志**:
```sql
-- 查看所有库存变更日志
SELECT il.*, p.name as product_name 
FROM inventory_logs il
JOIN products p ON il.product_id = p.id
ORDER BY il.created_at DESC;

-- 查看特定商品的库存变更
SELECT * FROM inventory_logs WHERE product_id = 1 ORDER BY created_at DESC;
```

**API接口**:
```http
GET /api/backup/inventory-logs?productId=1&page=1&size=10
```

### 2. 用户操作日志

**表名**: `user_operation_logs`

**查看日志**:
```sql
-- 查看所有用户操作日志
SELECT uol.*, u.username 
FROM user_operation_logs uol
JOIN users u ON uol.user_id = u.id
ORDER BY uol.created_at DESC;

-- 查看特定用户的操作日志
SELECT * FROM user_operation_logs WHERE user_id = 1 ORDER BY created_at DESC;
```

**API接口**:
```http
GET /api/backup/user-operation-logs?userId=1&page=1&size=10
```

## 🧪 测试功能

### 1. 运行单元测试

```bash
cd backend
mvn test -Dtest=DatabaseFunctionTest
```

### 2. 手动测试触发器

```sql
-- 1. 创建测试订单
INSERT INTO orders (user_id, order_number, total_amount, status, shipping_address) 
VALUES (1, 'TEST001', 15998.00, 'pending', '测试地址');

-- 2. 添加订单详情（触发库存扣减）
INSERT INTO order_items (order_id, product_id, quantity, price) 
VALUES (LAST_INSERT_ID(), 1, 2, 7999.00);

-- 3. 查看库存是否被扣减
SELECT id, name, stock FROM products WHERE id = 1;

-- 4. 查看库存变更日志
SELECT * FROM inventory_logs ORDER BY created_at DESC LIMIT 3;
```

### 3. 测试视图功能

```sql
-- 测试商品销售统计视图
SELECT * FROM v_product_sales_stats LIMIT 5;

-- 测试用户订单汇总视图
SELECT * FROM v_user_order_summary LIMIT 5;

-- 测试每日销售统计视图
SELECT * FROM v_daily_sales LIMIT 7;
```

## 📋 配置说明

### 1. 数据库配置

在 `backend/src/main/resources/application.yml` 中配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecom_management?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: 123456

app:
  backup:
    path: /backups  # 备份文件存储路径
```

### 2. 备份目录权限

确保备份目录有写入权限：

```bash
# 创建备份目录
sudo mkdir -p /backups

# 设置权限
sudo chown -R $USER:$USER /backups
sudo chmod 755 /backups
```

## 🚨 注意事项

1. **备份安全**: 备份文件包含敏感数据，请妥善保管
2. **权限控制**: 限制备份和恢复操作的访问权限
3. **磁盘空间**: 定期清理旧的备份文件
4. **测试环境**: 在生产环境使用前，请在测试环境充分验证
5. **数据一致性**: 恢复备份前请确保应用已停止服务

## 📞 技术支持

如有问题，请查看：
- [数据库功能说明文档](docs/数据库功能说明.md)
- [数据库功能流程图](docs/数据库功能流程图.md)
- 系统日志文件

---

**祝您使用愉快！** 🎉
