<template>
  <div class="login-container">
    <div class="login-form-wrapper">
      <h2 class="login-title">电商订单管理系统</h2>
      
      <!-- 账号密码登录表单 -->
      <el-form 
        :model="accountForm" 
        :rules="accountRules" 
        ref="accountFormRef"
        class="login-form"
      >
        <el-form-item prop="account">
          <el-input 
            v-model="accountForm.account" 
            placeholder="请输入账号（邮箱/手机号/用户名）"
            prefix-icon="el-icon-user"
          ></el-input>
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="accountForm.password" 
            type="password" 
            placeholder="请输入密码"
            prefix-icon="el-icon-lock"
            show-password
          ></el-input>
        </el-form-item>

        <!-- 用户类型选择 -->
        <el-form-item prop="userType">
          <el-radio-group v-model="accountForm.userType">
            <el-radio label="admin">管理员</el-radio>
            <el-radio label="user">普通用户</el-radio>
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
    const accountFormRef = ref()
    
    // 账号密码登录表单
    const accountForm = reactive({
      account: '',
      password: '',
      userType: 'user' // 默认普通用户
    })
    
    // 账号密码登录表单验证规则
    const accountRules = {
      account: [
        { required: true, message: '请输入账号', trigger: 'blur' }
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' }
      ],
      userType: [
        { required: true, message: '请选择用户类型', trigger: 'change' }
      ]
    }
    
    // 处理登录
    const handleLogin = async () => {
      if (!accountFormRef.value) return
      
      await accountFormRef.value.validate(async (valid) => {
        if (valid) {
          loading.value = true
          try {
            // 构造登录请求数据
            const loginData = {
              ...accountForm,
              // 这里可以添加额外的处理，根据userType设置对应的role值
              role: accountForm.userType === 'admin' ? 1 : 0
            }
            
            // 调用登录接口
            const res = await authApi.login(loginData)
            
            // 保存token和用户信息
            localStorage.setItem('token', res.data.token)
            localStorage.setItem('userInfo', JSON.stringify({
              id: res.data.id,
              username: res.data.username,
              email: res.data.email,
              phone: res.data.phone,
              role: res.data.role,
              userType: accountForm.userType // 保存用户选择的类型
            }))
            
            ElMessage.success('登录成功')
            
            // 根据用户类型跳转到不同页面
            if (accountForm.userType === 'admin') {
              // 管理员跳转到商品管理页面
              router.push('/product-management')
            } else {
              // 普通用户跳转到购物车页面
              router.push('/cart-management')
            }
          } catch (error) {
            ElMessage.error(error.message || '登录失败')
          } finally {
            loading.value = false
          }
        }
      })
    }
    
    return {
      loading,
      accountForm,
      accountRules,
      accountFormRef,
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