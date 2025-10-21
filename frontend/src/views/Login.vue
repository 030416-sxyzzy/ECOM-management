<template>
  <div class="login-container">
    <div class="login-form-wrapper">
      <h2 class="login-title">电商订单管理系统</h2>
      
      <!-- 账号密码登录表单 -->
      <el-form 
        :model="loginForm" 
        :rules="loginRules" 
        ref="loginFormRef"
        class="login-form"
      >
        <el-form-item prop="email">
          <el-input 
            v-model="loginForm.email" 
            placeholder="请输入邮箱"
            prefix-icon="el-icon-message"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            show-password
          ></el-input>
        </el-form-item>

        <!-- 用户类型选择 -->
        <el-form-item>
          <div style="text-align: center; margin-bottom: 10px; color: #606266;">请选择登录身份</div>
          <el-radio-group v-model="loginForm.role" style="width: 100%; display: flex; justify-content: space-around;">
            <el-radio label="USER">普通用户</el-radio>
            <el-radio label="ADMIN">管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            class="login-btn" 
            @click="handleLogin"
            :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <!-- 注册链接 -->
      <div class="register-link">
        还没有账号？<a href="/register">立即注册</a>
      </div>
    </div>
  </div>
</template>

<script>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import authApi from '../api/auth'
import { useRouter } from 'vue-router'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const loading = ref(false)
    const loginFormRef = ref()
    
    // 登录表单
    const loginForm = reactive({
      email: '',
      password: '',
      role: 'USER' // 默认普通用户
    })
    
    // 登录表单验证规则
    const loginRules = {
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ]
    }
    
    // 处理登录
    const handleLogin = async () => {
      if (!loginFormRef.value) return
      
      await loginFormRef.value.validate(async (valid) => {
        if (valid) {
          loading.value = true
          try {
            // 调用登录接口
            const response = await authApi.login({
              email: loginForm.email,
              password: loginForm.password
            })
            
            const userData = response.data.user
            const token = response.data.token
            
            // 检查用户实际角色与选择角色是否匹配
            if (userData.role !== loginForm.role) {
              ElMessage.error(`您的账号是${userData.role === 'ADMIN' ? '管理员' : '普通用户'}，请选择正确的登录身份`)
              loading.value = false
              return
            }
            
            // 保存token和用户信息
            localStorage.setItem('token', token)
            localStorage.setItem('user', JSON.stringify(userData))
            
            ElMessage.success('登录成功')
            
            // 根据用户角色跳转到不同页面
            if (userData.role === 'ADMIN') {
              // 管理员跳转到首页（ysy的商品管理和订单管理）
              router.push('/')
            } else {
              // 普通用户跳转到商品列表页（zsj的购物车和商品分类）
              router.push('/products')
            }
          } catch (error) {
            ElMessage.error(error.response?.data?.message || error.message || '登录失败')
          } finally {
            loading.value = false
          }
        }
      })
    }
    
    return {
      loading,
      loginForm,
      loginRules,
      loginFormRef,
      handleLogin
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
}

.login-form-wrapper {
  width: 400px;
  padding: 40px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
  font-size: 20px;
}

.login-form {
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #606266;
}

.register-link a {
  color: #409eff;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>