// 商品类型
export interface Product {
  id: number
  name: string
  description: string
  price: number
  stock: number
  categoryId: number
  imageUrl: string
  status: string
  createdAt: string
  updatedAt: string
}

// 商品分类类型
export interface Category {
  id: number
  name: string
  description: string
  createdAt: string
  updatedAt: string
}

// 购物车项类型
export interface CartItem {
  id: number
  userId: number
  productId: number
  quantity: number
  productName: string
  price: number
  imageUrl: string
  productStock: number
  productStatus: string
  createdAt: string
  updatedAt: string
}

// 搜索参数类型
export interface SearchParams {
  categoryId?: number
  keyword?: string
  minPrice?: number
  maxPrice?: number
  sortBy?: string
  sortOrder?: string
  pageNum?: number
  pageSize?: number
}

// API 响应类型
export interface ApiResponse<T = any> {
  success: boolean
  message: string
  data: T
}

// 分页数据类型
export interface PaginationData<T> {
  products: T[]
  total: number
  pageNum: number
  pageSize: number
}


