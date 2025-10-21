import axios from 'axios'
import type { ApiResponse, Product, Category, CartItem, SearchParams, PaginationData } from '@/types'

// 创建 axios 实例
const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 可以在这里添加 token 等认证信息
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

// 商品相关 API
export const productApi = {
  // 获取所有分类
  getCategories: (): Promise<ApiResponse<Category[]>> => {
    return api.get('/products/categories')
  },

  // 根据分类获取商品
  getProductsByCategory: (categoryId: number, pageNum = 1, pageSize = 10): Promise<ApiResponse<PaginationData<Product>>> => {
    return api.get(`/products/category/${categoryId}`, {
      params: { pageNum, pageSize }
    })
  },

  // 搜索商品
  searchProducts: (params: SearchParams): Promise<ApiResponse<PaginationData<Product>>> => {
    return api.get('/products/search', { params })
  },

  // 获取商品详情
  getProductDetail: (productId: number): Promise<ApiResponse<Product>> => {
    return api.get(`/products/${productId}`)
  },

  // 获取热门商品
  getHotProducts: (limit = 5): Promise<ApiResponse<Product[]>> => {
    return api.get('/products/hot', { params: { limit } })
  },

  // 获取推荐商品
  getRecommendedProducts: (limit = 5): Promise<ApiResponse<Product[]>> => {
    return api.get('/products/recommended', { params: { limit } })
  }
}

// 购物车相关 API
export const cartApi = {
  // 获取购物车商品
  getCartItems: (userId: number): Promise<ApiResponse<CartItem[]>> => {
    return api.get('/cart', { params: { userId } })
  },

  // 添加商品到购物车
  addToCart: (userId: number, productId: number, quantity: number): Promise<ApiResponse<CartItem[]>> => {
    return api.post('/cart/add', { productId, quantity }, { params: { userId } })
  },

  // 更新购物车商品数量
  updateCartItem: (cartItemId: number, userId: number, quantity: number): Promise<ApiResponse<CartItem[]>> => {
    return api.put(`/cart/${cartItemId}`, { quantity }, { params: { userId } })
  },

  // 删除购物车商品
  deleteCartItem: (cartItemId: number, userId: number): Promise<{ message: string }> => {
    return api.delete(`/cart/${cartItemId}`, { params: { userId } })
  },

  // 清空购物车
  clearCart: (userId: number): Promise<{ message: string }> => {
    return api.delete('/cart/clear', { params: { userId } })
  },

  // 获取购物车统计
  getCartStats: (userId: number): Promise<ApiResponse<{ totalItems: number; totalQuantity: number; totalAmount: number }>> => {
    return api.get('/cart/stats', { params: { userId } })
  }
}

export default api


