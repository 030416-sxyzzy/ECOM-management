# 电商管理系统

一个基于 Spring Boot + Vue3 + MySQL 的全栈电商订单管理系统。

## 🚀 技术栈

### 后端
- Spring Boot 3.2.0
- MyBatis 3.5.14
- MySQL 8.0
- Maven

### 前端
- Vue 3
- TypeScript
- Element Plus
- Pinia
- Vite

## 📦 功能模块

- ✅ 用户注册/登录
- ✅ 商品管理（分类、搜索、筛选、分页）
- ✅ 购物车管理（增删改查、批量结算）
- ✅ 订单管理（创建、状态流转、库存校验）
- ✅ 数据库触发器（自动库存扣减/恢复、操作日志）
- ✅ 数据库视图（销售统计、用户订单汇总）
- ✅ 数据库备份与恢复

## 🛠️ 快速开始

### 1. 数据库配置

```bash
# 创建数据库
mysql -u root -p

# 导入数据
source database/init.sql
```

### 2. 启动后端

```bash
cd backend
mvn spring-boot:run
```

后端服务将运行在 `http://localhost:8080`

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端服务将运行在 `http://localhost:3000`

## 📚 API 文档

详见 [API接口文档](docs/API接口文档.md)

## 🔧 配置说明

### 后端配置

编辑 `backend/src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ecom_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: your_password
```

### 前端配置

API地址配置在 `frontend/src/api/index.ts`:

```typescript
const API_BASE_URL = 'http://localhost:8080/api'
```

## 📝 项目结构

```
ecom-management/
├── backend/                 # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/       # Java源码
│   │   │   └── resources/  # 配置文件
│   └── pom.xml
├── frontend/               # 前端项目
│   ├── src/
│   │   ├── api/           # API接口
│   │   ├── components/    # 组件
│   │   ├── stores/        # 状态管理
│   │   ├── views/         # 页面
│   │   └── router/        # 路由
│   └── package.json
├── database/              # 数据库脚本
│   └── init.sql
└── docs/                  # 文档
    └── API接口文档.md
```

## 🎯 核心功能

### 商品管理
- 按分类浏览
- 关键词搜索
- 价格区间筛选（200以下/200~500/500以上）
- 分页展示

### 购物车
- 添加商品
- 修改数量（自动去重）
- 删除商品
- 清空购物车
- 实时统计

### 订单与数据库
- 事务化订单创建，下单前库存校验
- MySQL 触发器自动扣减/恢复库存
- 视图支撑销售统计与运营报表
- 支持数据库备份与恢复

## ⚠️ 注意事项

1. 确保 MySQL 服务已启动
2. 确保端口 8080 和 3000 未被占用
3. Node.js 版本建议 16.0+
4. Java 版本要求 17+

## 📄 License

MIT

---

**开发时间**: 2025年10月
