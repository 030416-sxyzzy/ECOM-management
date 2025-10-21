# 电商管理系统 API 接口文档

## 📋 项目概述
- **项目名称**: 电商管理系统
- **技术栈**: Spring Boot + MyBatis + MySQL
- **开发人员**: 张三（购物车管理 + 商品搜索模块）
- **更新时间**: 2025-10-16

## 🛒 购物车管理模块

### 1. 获取用户购物车
**接口地址**: `GET /api/cart`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**请求示例**:
```
GET http://localhost:8080/api/cart?userId=1
```

**响应示例**:
```json
{
  "success": true,
  "message": "获取购物车商品成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "productId": 1,
      "quantity": 2,
      "productName": "iPhone 15 Pro",
      "price": 7999.0,
      "imageUrl": "https://example.com/iphone.jpg",
      "productStock": 100,
      "productStatus": "active",
      "createdAt": "2024-01-01T00:00:00"
    }
  ]
}
```

### 2. 添加商品到购物车
**接口地址**: `POST /api/cart/add`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID（URL参数） |
| productId | Long | 是 | 商品ID（请求体） |
| quantity | Integer | 是 | 商品数量（请求体） |

**请求示例**:
```json
POST http://localhost:8080/api/cart/add?userId=1
Content-Type: application/json

{
  "productId": 2,
  "quantity": 1
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "商品添加购物车成功",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "productId": 1,
      "quantity": 2,
      "productName": "iPhone 15 Pro",
      "price": 7999.0
    }
  ]
}
```

### 3. 更新购物车商品数量
**接口地址**: `PUT /api/cart/{cartItemId}`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| cartItemId | Long | 是 | 购物车项ID（路径参数） |
| userId | Long | 是 | 用户ID（URL参数） |
| quantity | Integer | 是 | 新数量（请求体） |

**请求示例**:
```json
PUT http://localhost:8080/api/cart/1?userId=1
Content-Type: application/json

{
  "quantity": 3
}
```

### 4. 删除购物车商品
**接口地址**: `DELETE /api/cart/{cartItemId}`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| cartItemId | Long | 是 | 购物车项ID（路径参数） |
| userId | Long | 是 | 用户ID（URL参数） |

**请求示例**:
```
DELETE http://localhost:8080/api/cart/1?userId=1
```

### 5. 清空购物车
**接口地址**: `DELETE /api/cart/clear`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID（URL参数） |

**请求示例**:
```
DELETE http://localhost:8080/api/cart/clear?userId=1
```

### 6. 获取购物车统计信息
**接口地址**: `GET /api/cart/stats`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

**请求示例**:
```
GET http://localhost:8080/api/cart/stats?userId=1
```

**响应示例**:
```json
{
  "success": true,
  "message": "获取购物车统计信息成功",
  "data": {
    "totalItems": 2,
    "totalQuantity": 3,
    "totalAmount": 11997.0
  }
}
```

## 🔍 商品搜索模块

### 1. 获取商品分类
**接口地址**: `GET /api/products/categories`  
**请求参数**: 无

**请求示例**:
```
GET http://localhost:8080/api/products/categories
```

**响应示例**:
```json
{
  "success": true,
  "message": "获取商品分类成功",
  "data": [
    {
      "id": 1,
      "name": "电子产品",
      "description": "手机、电脑等电子设备",
      "createdAt": "2025-10-15T20:07:25",
      "updatedAt": "2025-10-15T20:07:25"
    }
  ]
}
```

### 2. 商品搜索
**接口地址**: `GET /api/products/search`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| categoryId | Long | 否 | 分类ID |
| keyword | String | 否 | 搜索关键词 |
| minPrice | BigDecimal | 否 | 最低价格 |
| maxPrice | BigDecimal | 否 | 最高价格 |
| sortBy | String | 否 | 排序字段（默认：created_at） |
| sortOrder | String | 否 | 排序方向（默认：desc） |
| pageNum | Integer | 否 | 页码（默认：1） |
| pageSize | Integer | 否 | 每页数量（默认：10） |

**请求示例**:
```
GET http://localhost:8080/api/products/search?keyword=手机&categoryId=1&minPrice=1000&maxPrice=10000&pageNum=1&pageSize=10
```

**响应示例**:
```json
{
  "success": true,
  "message": "商品搜索成功",
  "data": {
    "products": [
      {
        "id": 1,
        "name": "iPhone 15 Pro",
        "description": "苹果最新旗舰手机",
        "price": 7999.0,
        "stock": 50,
        "categoryId": 1,
        "imageUrl": "https://example.com/iphone.jpg",
        "status": "active",
        "createdAt": "2024-01-01T00:00:00",
        "updatedAt": "2024-01-01T00:00:00"
      }
    ],
    "total": 1,
    "pageNum": 1,
    "pageSize": 10
  }
}
```

### 3. 获取商品详情
**接口地址**: `GET /api/products/{productId}`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | Long | 是 | 商品ID（路径参数） |

**请求示例**:
```
GET http://localhost:8080/api/products/1
```

### 4. 获取热门商品
**接口地址**: `GET /api/products/hot`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | Integer | 否 | 返回数量（默认：5） |

**请求示例**:
```
GET http://localhost:8080/api/products/hot?limit=5
```

### 5. 获取推荐商品
**接口地址**: `GET /api/products/recommended`  
**请求参数**:
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| limit | Integer | 否 | 返回数量（默认：5） |

**请求示例**:
```
GET http://localhost:8080/api/products/recommended?limit=5
```

## 🚨 错误处理

### 通用错误响应格式
```json
{
  "success": false,
  "message": "错误描述信息",
  "data": null
}
```

### 常见错误码
| HTTP状态码 | 说明 |
|-----------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## 🔧 前端集成建议

### 1. 购物车页面功能
- **购物车列表**: 调用 `GET /api/cart` 显示商品
- **添加商品**: 调用 `POST /api/cart/add` 添加到购物车
- **修改数量**: 调用 `PUT /api/cart/{cartItemId}` 更新数量
- **删除商品**: 调用 `DELETE /api/cart/{cartItemId}` 删除商品
- **清空购物车**: 调用 `DELETE /api/cart/clear` 清空
- **显示统计**: 调用 `GET /api/cart/stats` 显示总价

### 2. 商品搜索页面功能
- **分类筛选**: 调用 `GET /api/products/categories` 获取分类
- **商品搜索**: 调用 `GET /api/products/search` 搜索商品
- **商品详情**: 调用 `GET /api/products/{productId}` 查看详情
- **热门推荐**: 调用 `GET /api/products/hot` 显示热门商品

### 3. 前端状态管理建议
```javascript
// 购物车状态
const cartState = {
  items: [],        // 购物车商品列表
  totalItems: 0,    // 总商品数
  totalAmount: 0    // 总金额
}

// 商品搜索状态
const searchState = {
  products: [],     // 搜索结果
  categories: [],   // 分类列表
  filters: {        // 搜索条件
    keyword: '',
    categoryId: null,
    minPrice: null,
    maxPrice: null
  }
}
```

## 📞 联系方式
- **开发者**: 张三
- **邮箱**: zhangsan@example.com
- **更新时间**: 2025-10-16

---
*本文档会随着功能更新持续维护*

