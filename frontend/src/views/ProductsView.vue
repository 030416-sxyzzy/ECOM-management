<template>
  <div class="products">
    <div class="products-container">
      <!-- 侧边栏筛选 -->
      <div class="sidebar">
        <div class="filter-section">
          <h3>商品分类</h3>
          <el-menu
            :default-active="selectedCategory?.toString()"
            @select="handleCategorySelect"
          >
            <el-menu-item :index="0">全部商品</el-menu-item>
            <el-menu-item
              v-for="category in categories"
              :key="category.id"
              :index="category.id.toString()"
            >
              {{ category.name }}
            </el-menu-item>
          </el-menu>
        </div>

        <div class="filter-section">
          <h3>价格区间</h3>
          <el-radio-group v-model="priceRange" @change="handlePriceRangeChange">
            <el-radio label="all" class="price-radio">全部价格</el-radio>
            <el-radio label="low" class="price-radio">200元以下</el-radio>
            <el-radio label="medium" class="price-radio">200~500元</el-radio>
            <el-radio label="high" class="price-radio">500元以上</el-radio>
          </el-radio-group>
        </div>

        <el-button type="primary" @click="searchProducts" style="width: 100%; margin-top: 20px;">
          应用筛选
        </el-button>
      </div>

      <!-- 主内容区域 -->
      <div class="main-content">
        <!-- 搜索栏 -->
        <div class="search-bar">
          <el-input
            v-model="searchParams.keyword"
            placeholder="搜索商品名称..."
            @keyup.enter="searchProducts"
            @input="handleSearchInput"
            clearable
          >
            <template #append>
              <el-button @click="searchProducts">
                <el-icon><Search /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>

        <!-- 商品列表 -->
        <div class="product-list">
          <div v-if="loading" class="loading">
            <el-skeleton :rows="6" animated />
          </div>
          
          <div v-else-if="products.length === 0" class="empty">
            <el-empty description="暂无商品" />
          </div>

          <div v-else class="product-grid">
            <div
              v-for="product in products"
              :key="product.id"
              class="product-card"
              @click="goToProductDetail(product.id)"
            >
              <div class="product-image">
                <img :src="product.imageUrl" :alt="product.name" />
              </div>
              <div class="product-info">
                <h3>{{ product.name }}</h3>
                <p class="product-description">{{ product.description }}</p>
                <p class="product-price">¥{{ product.price }}</p>
                <p class="product-stock">库存: {{ product.stock }}</p>
                <el-button
                  type="primary"
                  size="small"
                  @click.stop="addToCart(product.id)"
                >
                  加入购物车
                </el-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div v-if="total > 0" class="pagination">
          <el-pagination
            v-model:current-page="searchParams.pageNum"
            v-model:page-size="searchParams.pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { Category, Product, SearchParams } from '@/types'
import { productApi } from '@/api'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

// 数据
const categories = ref<Category[]>([])
const products = ref<Product[]>([])
const loading = ref(false)
const total = ref(0)

// 搜索参数
const searchParams = reactive<SearchParams>({
  categoryId: undefined,
  keyword: '',
  minPrice: undefined,
  maxPrice: undefined,
  sortBy: 'created_at',
  sortOrder: 'desc',
  pageNum: 1,
  pageSize: 10
})

const selectedCategory = ref<number | null>(null)
const priceRange = ref<string>('all')

// 生命周期
onMounted(async () => {
  await loadCategories()
  await searchProducts()
  
  // 处理URL参数
  if (route.query.category) {
    const categoryId = parseInt(route.query.category as string)
    searchParams.categoryId = categoryId
    selectedCategory.value = categoryId
  }
})

// 监听搜索参数变化
watch(() => route.query, (newQuery) => {
  if (newQuery.category) {
    const categoryId = parseInt(newQuery.category as string)
    searchParams.categoryId = categoryId
    selectedCategory.value = categoryId
    searchProducts()
  }
})

// 加载分类
const loadCategories = async () => {
  try {
    const response = await productApi.getCategories()
    if (response.success) {
      categories.value = response.data
    }
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

// 搜索商品
const searchProducts = async () => {
  loading.value = true
  try {
    const response = await productApi.searchProducts(searchParams)
    if (response.success) {
      products.value = response.data.products
      total.value = response.data.total
    } else {
      ElMessage.error(response.message || '搜索失败')
    }
  } catch (error) {
    console.error('搜索商品失败:', error)
    ElMessage.error('搜索失败，请重试')
  } finally {
    loading.value = false
  }
}

// 分类选择
const handleCategorySelect = (categoryId: string) => {
  const id = parseInt(categoryId)
  if (id === 0) {
    searchParams.categoryId = undefined
    selectedCategory.value = null
  } else {
    searchParams.categoryId = id
    selectedCategory.value = id
  }
  searchParams.pageNum = 1
  searchProducts()
}

// 价格区间选择（不自动触发搜索）
const handlePriceRangeChange = (value: string) => {
  switch (value) {
    case 'all':
      searchParams.minPrice = undefined
      searchParams.maxPrice = undefined
      break
    case 'low':
      searchParams.minPrice = undefined
      searchParams.maxPrice = 200
      break
    case 'medium':
      searchParams.minPrice = 200
      searchParams.maxPrice = 500
      break
    case 'high':
      searchParams.minPrice = 500
      searchParams.maxPrice = undefined
      break
  }
  searchParams.pageNum = 1
  // 不再自动调用 searchProducts()，等待用户点击"应用筛选"
}

// 搜索框输入监听
const handleSearchInput = (value: string) => {
  // 如果搜索框被清空，自动恢复显示全部商品
  if (!value || value.trim() === '') {
    searchParams.keyword = ''
    searchProducts()
  }
}

// 分页大小变化
const handleSizeChange = (size: number) => {
  searchParams.pageSize = size
  searchParams.pageNum = 1
  searchProducts()
}

// 当前页变化
const handleCurrentChange = (page: number) => {
  searchParams.pageNum = page
  searchProducts()
}

// 跳转到商品详情
const goToProductDetail = (productId: number) => {
  router.push(`/products/${productId}`)
}

// 添加到购物车
const addToCart = async (productId: number) => {
  const success = await cartStore.addToCart(productId, 1)
  if (success) {
    ElMessage.success('商品已添加到购物车')
  } else {
    ElMessage.error('添加失败，请重试')
  }
}
</script>

<style scoped>
.products {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.products-container {
  display: flex;
  gap: 20px;
}

.sidebar {
  width: 250px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  height: fit-content;
}

.filter-section {
  margin-bottom: 30px;
}

.filter-section h3 {
  margin: 0 0 15px 0;
  color: #333;
  font-size: 1.1rem;
}

.filter-section .el-radio-group {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.filter-section .price-radio {
  height: auto;
  margin-right: 0;
}

.main-content {
  flex: 1;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.search-bar {
  margin-bottom: 20px;
}

.product-list {
  margin-bottom: 20px;
}

.loading {
  padding: 20px;
}

.empty {
  padding: 40px;
  text-align: center;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.product-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.product-image {
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.product-info h3 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 1rem;
  line-height: 1.4;
}

.product-description {
  color: #666;
  font-size: 0.9rem;
  margin: 0 0 10px 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-price {
  font-size: 1.2rem;
  font-weight: bold;
  color: #e6a23c;
  margin: 0 0 5px 0;
}

.product-stock {
  color: #666;
  font-size: 0.9rem;
  margin: 0 0 15px 0;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>

