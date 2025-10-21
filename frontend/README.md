# 电商管理系统前端

基于 Vue 3 + TypeScript + Element Plus 的现代化电商管理系统前端应用。

## 🚀 技术栈

- **框架**: Vue 3.4.0
- **语言**: TypeScript
- **UI库**: Element Plus 2.4.4
- **状态管理**: Pinia 2.1.7
- **路由**: Vue Router 4.2.5
- **构建工具**: Vite 5.0.8
- **HTTP客户端**: Axios 1.6.2

## 📁 项目结构

```
frontend/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API接口
│   │   └── index.ts
│   ├── components/        # 公共组件
│   │   └── ProductCard.vue
│   ├── router/            # 路由配置
│   │   └── index.ts
│   ├── stores/            # Pinia状态管理
│   │   └── cart.ts
│   ├── types/             # TypeScript类型定义
│   │   └── index.ts
│   ├── utils/             # 工具函数
│   │   └── index.ts
│   ├── views/             # 页面组件
│   │   ├── HomeView.vue
│   │   ├── ProductsView.vue
│   │   ├── ProductDetailView.vue
│   │   └── CartView.vue
│   ├── App.vue            # 根组件
│   └── main.ts            # 入口文件
├── index.html             # HTML模板
├── package.json           # 依赖配置
├── tsconfig.json          # TypeScript配置
├── tsconfig.node.json     # Node.js TypeScript配置
└── vite.config.ts         # Vite配置
```

## 🛠️ 开发环境

### 环境要求

- Node.js >= 16.0.0
- npm >= 7.0.0

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

访问 http://localhost:3000

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 📱 功能特性

### 已实现功能

- ✅ **首页展示**
  - 轮播图展示
  - 商品分类导航
  - 热门商品推荐
  - 推荐商品展示

- ✅ **商品浏览**
  - 商品列表展示
  - 分类筛选
  - 关键词搜索
  - 价格区间筛选
  - 排序功能
  - 分页展示

- ✅ **商品详情**
  - 商品详细信息
  - 图片展示
  - 库存状态
  - 数量选择
  - 加入购物车
  - 立即购买

- ✅ **购物车管理**
  - 购物车商品列表
  - 数量调整
  - 商品删除
  - 全选/取消全选
  - 价格计算
  - 清空购物车

### 技术特性

- 🎨 **现代化UI**: 基于 Element Plus 的美观界面
- 📱 **响应式设计**: 支持桌面端和移动端
- 🔧 **TypeScript**: 完整的类型安全
- 🚀 **性能优化**: Vite 快速构建，组件懒加载
- 🎯 **状态管理**: Pinia 集中式状态管理
- 🔄 **API集成**: 与后端 API 完整对接

## 🔌 API 集成

前端与后端 API 完全对接，支持以下接口：

- 商品分类管理
- 商品搜索和筛选
- 商品详情获取
- 购物车增删改查
- 购物车统计信息

## 🎨 界面预览

### 首页
- 轮播图展示
- 商品分类网格
- 热门商品卡片
- 推荐商品展示

### 商品页面
- 侧边栏筛选
- 搜索功能
- 商品网格展示
- 分页导航

### 商品详情
- 商品图片展示
- 详细信息
- 规格参数
- 操作按钮

### 购物车
- 商品列表
- 数量调整
- 价格计算
- 结算功能

## 🚀 部署说明

1. 构建生产版本：
   ```bash
   npm run build
   ```

2. 将 `dist` 目录部署到 Web 服务器

3. 配置 Nginx 反向代理到后端 API

## 📝 开发说明

### 添加新页面

1. 在 `src/views/` 创建 Vue 组件
2. 在 `src/router/index.ts` 添加路由
3. 在 `src/types/index.ts` 添加类型定义

### 添加新 API

1. 在 `src/api/index.ts` 添加 API 方法
2. 在 `src/stores/` 添加状态管理
3. 在组件中调用 API

### 添加新组件

1. 在 `src/components/` 创建组件
2. 导出组件类型
3. 在需要的地方导入使用

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 📄 许可证

MIT License


