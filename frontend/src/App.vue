<template>
  <div id="app">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="header">
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
              <el-menu-item index="/">首页</el-menu-item>
              <el-menu-item index="/cart">购物车</el-menu-item>
            </el-menu>
          </div>
          <div class="user-actions">
            <el-button type="primary" @click="goToCart">
              <el-icon><ShoppingCart /></el-icon>
              购物车 ({{ cartCount }})
            </el-button>
          </div>
        </div>
      </el-header>

      <!-- 主要内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>

      <!-- 底部 -->
      <el-footer class="footer">
        <p>&copy; 2024 电商管理系统. All rights reserved.</p>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const cartStore = useCartStore()

// 使用computed计算购物车数量，自动响应变化
const cartCount = computed(() => cartStore.totalQuantity)

// 页面加载时获取购物车数据
onMounted(async () => {
  await cartStore.fetchCartItems()
})

// 跳转到购物车页面
const goToCart = () => {
  router.push('/cart')
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

