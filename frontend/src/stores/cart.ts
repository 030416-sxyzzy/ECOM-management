import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { CartItem } from '@/types'
import { cartApi } from '@/api'

export const useCartStore = defineStore('cart', () => {
  // 状态
  const cartItems = ref<CartItem[]>([])
  const userId = ref<number>(1) // 临时固定用户ID，实际应该从登录状态获取

  // 计算属性
  const totalQuantity = computed(() => {
    return cartItems.value.reduce((total, item) => total + item.quantity, 0)
  })

  const totalAmount = computed(() => {
    return cartItems.value.reduce((total, item) => total + (item.price * item.quantity), 0)
  })

  const totalItems = computed(() => {
    return cartItems.value.length
  })

  // 动作
  const fetchCartItems = async () => {
    try {
      const response = await cartApi.getCartItems(userId.value)
      if (response.success) {
        cartItems.value = response.data
      }
    } catch (error) {
      console.error('获取购物车失败:', error)
    }
  }

  const addToCart = async (productId: number, quantity: number = 1) => {
    try {
      const response = await cartApi.addToCart(userId.value, productId, quantity)
      if (response.success) {
        cartItems.value = response.data
        return true
      }
      return false
    } catch (error) {
      console.error('添加商品到购物车失败:', error)
      return false
    }
  }

  const updateCartItem = async (cartItemId: number, quantity: number) => {
    try {
      const response = await cartApi.updateCartItem(cartItemId, userId.value, quantity)
      if (response.success) {
        cartItems.value = response.data
        return true
      }
      return false
    } catch (error) {
      console.error('更新购物车商品失败:', error)
      return false
    }
  }

  const deleteCartItem = async (cartItemId: number) => {
    try {
      await cartApi.deleteCartItem(cartItemId, userId.value)
      await fetchCartItems() // 重新获取购物车数据
      return true
    } catch (error) {
      console.error('删除购物车商品失败:', error)
      return false
    }
  }

  const clearCart = async () => {
    try {
      await cartApi.clearCart(userId.value)
      cartItems.value = []
      return true
    } catch (error) {
      console.error('清空购物车失败:', error)
      return false
    }
  }

  return {
    // 状态
    cartItems,
    userId,
    // 计算属性
    totalQuantity,
    totalAmount,
    totalItems,
    // 动作
    fetchCartItems,
    addToCart,
    updateCartItem,
    deleteCartItem,
    clearCart
  }
})

