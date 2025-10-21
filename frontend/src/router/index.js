import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: {
      title: '登录'
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: {
      title: '注册'
    }
  },
  {
    path: '/home',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: {
      title: '首页',
      requiresAuth: true
    }
  },
  // 管理员页面
  {
    path: '/product-management',
    name: 'ProductManagement',
    component: () => import('../views/ProductManagement.vue'),
    meta: {
      title: '商品管理',
      requiresAuth: true,
      requiresAdmin: true
    }
  },
  {
    path: '/order-management',
    name: 'OrderManagement',
    component: () => import('../views/OrderManagement.vue'),
    meta: {
      title: '订单管理',
      requiresAuth: true,
      requiresAdmin: true
    }
  },
  // 普通用户页面
  {
    path: '/cart-management',
    name: 'CartManagement',
    component: () => import('../views/CartManagement.vue'),
    meta: {
      title: '购物车管理',
      requiresAuth: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title || '电商订单管理系统'
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    const token = localStorage.getItem('token')
    if (!token) {
      // 未登录，跳转到登录页
      next({ path: '/login' })
      return
    }
    
    // 检查是否需要管理员权限
    if (to.meta.requiresAdmin) {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      // 检查用户类型或角色
      const isAdmin = userInfo && (userInfo.userType === 'admin' || userInfo.role === 1)
      if (!isAdmin) {
        // 不是管理员，跳转到购物车页面
        next({ path: '/cart-management' })
        return
      }
    }
    
    // 已登录且权限符合要求，继续访问
    next()
  } else {
    // 不需要认证，直接访问
    next()
  }
})

export default router