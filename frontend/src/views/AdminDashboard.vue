<template>
  <div class="admin-dashboard">
    <el-card class="header-card">
      <div class="welcome-section">
        <div class="welcome-info">
          <h2>欢迎，管理员 {{ userName }}</h2>
          <p>电商订单管理系统 - 后台管理中心</p>
        </div>
        <el-button type="danger" @click="logout">退出登录</el-button>
      </div>
    </el-card>

    <el-tabs v-model="activeTab" class="main-tabs">
      <!-- 商品管理 -->
      <el-tab-pane label="商品管理" name="products">
        <ProductManagement />
      </el-tab-pane>

      <!-- 订单管理 -->
      <el-tab-pane label="订单管理" name="orders">
        <OrderManagement />
      </el-tab-pane>
      
      <!-- 销量统计 -->
      <el-tab-pane label="📊 销量统计" name="stats">
        <SalesStats />
      </el-tab-pane>
      
      <!-- 数据库备份 -->
      <el-tab-pane label="💾 数据库备份" name="backup">
        <DatabaseBackup />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import ProductManagement from '../components/ProductManagement.vue'
import OrderManagement from '../components/OrderManagement.vue'
import SalesStats from '../components/SalesStats.vue'
import DatabaseBackup from '../components/DatabaseBackup.vue'

const router = useRouter()
const activeTab = ref('products')

// 获取当前用户名
const userName = computed(() => {
  const user = localStorage.getItem('user')
  if (user) {
    const userObj = JSON.parse(user)
    return userObj.username || userObj.email
  }
  return '管理员'
})

// 退出登录
const logout = () => {
  // 清除本地存储的用户信息和token
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  
  // 跳转到登录页
  router.push('/login')
}
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
  background: #f5f5f5;
  min-height: calc(100vh - 140px);
}

.header-card {
  margin-bottom: 20px;
}

.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-info h2 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 24px;
}

.welcome-info p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.main-tabs {
  background: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

:deep(.el-tabs__header) {
  margin-bottom: 20px;
}

:deep(.el-tabs__item) {
  font-size: 16px;
  font-weight: 500;
}
</style>

