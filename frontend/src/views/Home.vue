<template>
  <div class="home-container">
    <div class="header">
      <div class="header-left">
        <h1 class="system-name">电商订单管理系统</h1>
      </div>
      <div class="header-right">
        <span class="user-info">欢迎您，{{ userInfo.username || '用户' }}</span>
        <el-button type="text" @click="handleLogout">退出登录</el-button>
      </div>
    </div>
    
    <div class="content">
      <div class="dashboard">
        <h2>欢迎使用电商订单管理系统</h2>
        <div class="role-info">
          <p>您的账号类型: {{ getUserTypeText() }}</p>
        </div>
        
        <!-- 管理员功能卡片 -->
        <div v-if="isAdmin" class="feature-list">
          <div class="feature-card" @click="navigateTo('/product-management')">
            <el-icon class="feature-icon"><Goods /></el-icon>
            <h3>商品管理</h3>
            <p>管理商品信息，添加、编辑、删除商品</p>
          </div>
          
          <div class="feature-card" @click="navigateTo('/order-management')">
            <el-icon class="feature-icon"><ShoppingCart /></el-icon>
            <h3>订单管理</h3>
            <p>查看和处理所有用户订单</p>
          </div>
        </div>
        
        <!-- 普通用户功能卡片 -->
        <div v-else class="feature-list">
          <div class="feature-card" @click="navigateTo('/cart-management')">
            <el-icon class="feature-icon"><ShoppingCart /></el-icon>
            <h3>购物车</h3>
            <p>管理您的购物车商品</p>
          </div>
        </div>
        
        <div class="quick-tips">
          <h3>快速提示</h3>
          <p v-if="isAdmin">作为管理员，您可以管理系统中的所有商品和订单。</p>
          <p v-else>作为普通用户，您可以在购物车中管理您想要购买的商品。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import authApi from '../api/auth'
import { ShoppingCart, Goods } from '@element-plus/icons-vue'

export default {
  name: 'Home',
  components: {
    ShoppingCart,
    Goods
  },
  setup() {
    const router = useRouter()
    const userInfo = ref({
      username: '',
      role: 0,
      userType: 'user'
    })
    
    // 判断是否为管理员
    const isAdmin = computed(() => {
      return userInfo.value.userType === 'admin' || userInfo.value.role === 1
    })
    
    // 获取用户类型文本
    const getUserTypeText = () => {
      return isAdmin.value ? '管理员' : '普通用户'
    }
    
    // 页面导航
    const navigateTo = (path) => {
      router.push(path)
    }
    
    // 获取用户信息
    const getUserInfo = () => {
      try {
        const token = localStorage.getItem('token')
        const userData = localStorage.getItem('userInfo')
        
        if (!token) {
          router.push('/login')
          return
        }
        
        if (userData) {
          userInfo.value = JSON.parse(userData)
        }
      } catch (error) {
        console.error('获取用户信息失败:', error)
        ElMessage.error('获取用户信息失败')
      }
    }
    
    // 退出登录
    const handleLogout = async () => {
      try {
        await authApi.logout()
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        ElMessage.success('退出成功')
        router.push('/login')
      } catch (error) {
        // 即使退出接口调用失败，也清除本地数据并跳转
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        router.push('/login')
      }
    }
   // 初始化
    onMounted(() => {
      getUserInfo()
    })
    
    return {
      userInfo,
      isAdmin,
      getUserTypeText,
      navigateTo,
      handleLogout
    }
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.header {
  height: 60px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.system-name {
  font-size: 20px;
  color: #303133;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.user-info {
  color: #606266;
}

.content {
  padding: 20px;
}

.dashboard {
  background-color: #fff;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.dashboard h2 {
  margin-bottom: 20px;
  color: #303133;
}

.role-info {
  margin-bottom: 30px;
  padding: 15px;
  background-color: #ecf5ff;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.role-info p {
  margin: 0;
  color: #606266;
}

.feature-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.feature-card {
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  text-align: center;
  transition: transform 0.3s ease;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.feature-icon {
  font-size: 48px;
  color: #409eff;
  margin-bottom: 15px;
}

.feature-card h3 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 18px;
}

.feature-card p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}
  
  .quick-tips {
    background-color: #f0f9eb;
    border: 1px solid #c2e7b0;
    border-radius: 4px;
    padding: 15px;
    margin-top: 30px;
  }
  
  .quick-tips h3 {
    margin-top: 0;
    color: #67c23a;
    font-size: 16px;
  }
  
  .quick-tips p {
    margin-bottom: 0;
    color: #606266;
  }
}
</style>