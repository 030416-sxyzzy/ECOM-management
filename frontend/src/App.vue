<template>
  <div id="app">
    <el-container>
      <!-- 顶部导航栏 - 只在登录后显示 -->
      <el-header v-if="showHeader" class="header">
        <div class="header-content">
          <div class="logo">
            <h2>🛒 电商管理系统</h2>
          </div>
          <div class="nav-menu">
            <el-menu
              mode="horizontal"
              :default-active="$route.path"
              router
              class="nav-menu"
            >
              <el-menu-item index="/products">首页</el-menu-item>
            </el-menu>
          </div>
          <div class="user-actions">
            <el-button type="primary" @click="goToCart">
              <el-icon><ShoppingCart /></el-icon>
              购物车 ({{ cartCount }})
            </el-button>
            <el-button @click="logout">退出登录</el-button>
          </div>
        </div>
      </el-header>

      <!-- 主要内容区域 -->
      <el-main :class="showHeader ? 'main-content' : 'main-content-full'">
        <router-view />
      </el-main>

      <!-- 底部 - 只在登录后显示 -->
      <el-footer v-if="showHeader" class="footer">
        <p>&copy; 2024 电商管理系统. All rights reserved.</p>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()

// 判断是否显示导航栏（登录页、注册页、管理后台不显示）
const showHeader = computed(() => {
  const noHeaderPages = ['/login', '/register', '/', '/admin']
  return !noHeaderPages.includes(route.path)
})

// 使用computed计算购物车数量，自动响应变化
const cartCount = computed(() => cartStore.totalQuantity)

// 页面加载时获取购物车数据（只在登录后加载）
onMounted(async () => {
  if (showHeader.value) {
    await cartStore.fetchCartItems()
  }
})

// 监听路由变化，在进入需要导航栏的页面时加载购物车数据
watch(() => route.path, async (newPath) => {
  if (showHeader.value && cartStore.totalQuantity === 0) {
    await cartStore.fetchCartItems()
  }
})

// 跳转到购物车页面
const goToCart = () => {
  router.push('/cart')
}

// 退出登录
const logout = () => {
  // 清除本地存储的用户信息和token
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  
  // 清空购物车数据（直接修改数组）
  cartStore.cartItems.splice(0)
  
  // 跳转到登录页
  router.push('/login')
}
</script>

<style scoped>
.header {
  background: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.logo h2 {
  margin: 0;
  color: #409eff;
}

.nav-menu {
  flex: 1;
  margin: 0 40px;
  border-bottom: none;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.main-content {
  min-height: calc(100vh - 120px);
  background: #f5f5f5;
}

.main-content-full {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 0;
}

.footer {
  background: #333;
  color: #fff;
  text-align: center;
  padding: 20px 0;
}

.footer p {
  margin: 0;
}
</style>

