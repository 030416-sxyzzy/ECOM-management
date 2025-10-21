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
  // 管理后台
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: () => import('../views/AdminDashboard.vue'),
    meta: {
      title: '管理后台',
      requiresAuth: true,
      requiresAdmin: true
    }
  },
  // 商品浏览
  {
    path: '/products',
    name: 'Products',
    component: () => import('../views/ProductsView.vue'),
    meta: {
      title: '商品列表',
      requiresAuth: true
    }
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: () => import('../views/ProductDetailView.vue'),
    meta: {
      title: '商品详情',
      requiresAuth: true
    }
  },
  // 购物车
  {
    path: '/cart',
    name: 'Cart',
    component: () => import('../views/CartView.vue'),
    meta: {
      title: '购物车',
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
      const user = localStorage.getItem('user')
      if (user) {
        const userObj = JSON.parse(user)
        const isAdmin = userObj.role === 'ADMIN'
        if (!isAdmin) {
          // 不是管理员，跳转到商品列表页面
          next({ path: '/products' })
          return
        }
      } else {
        // 没有用户信息，跳转到登录页
        next({ path: '/login' })
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