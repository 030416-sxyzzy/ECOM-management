<template>
  <div class="product-detail">
    <div v-if="loading" class="loading">
      <el-skeleton :rows="8" animated />
    </div>

    <div v-else-if="!product" class="empty">
      <el-empty description="商品不存在" />
    </div>

    <div v-else class="detail-container">
      <!-- 商品图片和基本信息 -->
      <div class="product-main">
        <div class="product-image">
          <img :src="product.imageUrl" :alt="product.name" />
        </div>
        
        <div class="product-info">
          <h1 class="product-name">{{ product.name }}</h1>
          <p class="product-description">{{ product.description }}</p>
          
          <div class="price-section">
            <span class="current-price">¥{{ product.price }}</span>
          </div>

          <div class="stock-section">
            <span class="stock-label">库存：</span>
            <span class="stock-count" :class="{ 'low-stock': product.stock < 10 }">
              {{ product.stock }} 件
            </span>
          </div>

          <div class="quantity-section">
            <span class="quantity-label">数量：</span>
            <el-input-number
              v-model="quantity"
              :min="1"
              :max="product.stock"
              :disabled="product.stock === 0"
            />
          </div>

          <div class="action-buttons">
            <el-button
              type="primary"
              size="large"
              :disabled="product.stock === 0"
              @click="addToCart"
            >
              <el-icon><ShoppingCart /></el-icon>
              加入购物车
            </el-button>
            <el-button
              type="success"
              size="large"
              :disabled="product.stock === 0"
              @click="buyNow"
            >
              立即购买
            </el-button>
          </div>

          <div class="product-meta">
            <p><strong>商品ID：</strong>{{ product.id }}</p>
            <p><strong>上架时间：</strong>{{ formatDate(product.createdAt) }}</p>
            <p><strong>更新时间：</strong>{{ formatDate(product.updatedAt) }}</p>
          </div>
        </div>
      </div>

      <!-- 商品详情描述 -->
      <div class="product-details">
        <el-tabs v-model="activeTab">
          <el-tab-pane label="商品详情" name="details">
            <div class="details-content">
              <h3>商品详情</h3>
              <p>{{ product.description }}</p>
              <div class="detail-images">
                <img
                  v-for="(image, index) in detailImages"
                  :key="index"
                  :src="image"
                  :alt="`详情图${index + 1}`"
                  class="detail-image"
                />
              </div>
            </div>
          </el-tab-pane>
          
          <el-tab-pane label="规格参数" name="specs">
            <div class="specs-content">
              <h3>规格参数</h3>
              <el-table :data="specs" border>
                <el-table-column prop="name" label="参数名称" width="200" />
                <el-table-column prop="value" label="参数值" />
              </el-table>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- 推荐商品 -->
      <div class="recommended-products">
        <h3>推荐商品</h3>
        <div class="recommended-grid">
          <div
            v-for="item in recommendedProducts"
            :key="item.id"
            class="recommended-card"
            @click="goToProductDetail(item.id)"
          >
            <div class="recommended-image">
              <img :src="item.imageUrl" :alt="item.name" />
            </div>
            <div class="recommended-info">
              <h4>{{ item.name }}</h4>
              <p class="recommended-price">¥{{ item.price }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { Product } from '@/types'
import { productApi } from '@/api'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()

// 数据
const product = ref<Product | null>(null)
const loading = ref(false)
const quantity = ref(1)
const activeTab = ref('details')
const recommendedProducts = ref<Product[]>([])

// 详情图片
const detailImages = ref([
  'https://via.placeholder.com/600x400/f0f0f0/999999?text=详情图1',
  'https://via.placeholder.com/600x400/f0f0f0/999999?text=详情图2',
  'https://via.placeholder.com/600x400/f0f0f0/999999?text=详情图3'
])

// 规格参数
const specs = ref([
  { name: '品牌', value: '示例品牌' },
  { name: '型号', value: '示例型号' },
  { name: '颜色', value: '多种颜色可选' },
  { name: '尺寸', value: '标准尺寸' },
  { name: '重量', value: '约1kg' },
  { name: '材质', value: '优质材料' }
])

// 计算属性
const productId = computed(() => {
  return parseInt(route.params.id as string)
})

// 生命周期
onMounted(async () => {
  await loadProductDetail()
  await loadRecommendedProducts()
})

// 加载商品详情
const loadProductDetail = async () => {
  loading.value = true
  try {
    const response = await productApi.getProductDetail(productId.value)
    if (response.success) {
      product.value = response.data
    } else {
      ElMessage.error(response.message || '商品不存在')
    }
  } catch (error) {
    console.error('加载商品详情失败:', error)
    ElMessage.error('加载商品详情失败')
  } finally {
    loading.value = false
  }
}

// 加载推荐商品
const loadRecommendedProducts = async () => {
  try {
    const response = await productApi.getRecommendedProducts(4)
    if (response.success) {
      recommendedProducts.value = response.data.filter(item => item.id !== productId.value)
    }
  } catch (error) {
    console.error('加载推荐商品失败:', error)
  }
}

// 添加到购物车
const addToCart = async () => {
  if (!product.value) return
  
  const success = await cartStore.addToCart(product.value.id, quantity.value)
  if (success) {
    ElMessage.success(`已添加 ${quantity.value} 件商品到购物车`)
  } else {
    ElMessage.error('添加失败，请重试')
  }
}

// 立即购买
const buyNow = async () => {
  if (!product.value) return
  
  // 先添加到购物车
  const success = await cartStore.addToCart(product.value.id, quantity.value)
  if (success) {
    // 跳转到购物车页面
    router.push('/cart')
  } else {
    ElMessage.error('操作失败，请重试')
  }
}

// 跳转到商品详情
const goToProductDetail = (productId: number) => {
  router.push(`/products/${productId}`)
}

// 格式化日期
const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}
</script>

<style scoped>
.product-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading {
  padding: 40px;
}

.empty {
  padding: 40px;
  text-align: center;
}

.detail-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.product-main {
  display: flex;
  gap: 40px;
  padding: 40px;
  border-bottom: 1px solid #e4e7ed;
}

.product-image {
  flex: 0 0 400px;
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
}

.product-name {
  font-size: 2rem;
  color: #333;
  margin: 0 0 15px 0;
  line-height: 1.3;
}

.product-description {
  color: #666;
  font-size: 1.1rem;
  line-height: 1.6;
  margin: 0 0 20px 0;
}

.price-section {
  margin: 20px 0;
}

.current-price {
  font-size: 2.5rem;
  font-weight: bold;
  color: #e6a23c;
}

.stock-section {
  margin: 20px 0;
  font-size: 1.1rem;
}

.stock-label {
  color: #666;
}

.stock-count {
  color: #67c23a;
  font-weight: bold;
}

.stock-count.low-stock {
  color: #f56c6c;
}

.quantity-section {
  margin: 20px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.quantity-label {
  color: #666;
  font-size: 1.1rem;
}

.action-buttons {
  margin: 30px 0;
  display: flex;
  gap: 15px;
}

.product-meta {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.product-meta p {
  margin: 5px 0;
  color: #666;
  font-size: 0.9rem;
}

.product-details {
  padding: 40px;
}

.details-content h3,
.specs-content h3 {
  margin: 0 0 20px 0;
  color: #333;
}

.detail-images {
  margin-top: 20px;
}

.detail-image {
  width: 100%;
  max-width: 600px;
  margin: 10px 0;
  border-radius: 4px;
}

.specs-content {
  margin-top: 20px;
}

.recommended-products {
  padding: 40px;
  border-top: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.recommended-products h3 {
  margin: 0 0 20px 0;
  color: #333;
}

.recommended-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.recommended-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.recommended-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.recommended-image {
  height: 150px;
  overflow: hidden;
}

.recommended-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.recommended-info {
  padding: 15px;
}

.recommended-info h4 {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 0.9rem;
  line-height: 1.4;
}

.recommended-price {
  margin: 0;
  color: #e6a23c;
  font-weight: bold;
  font-size: 1rem;
}
</style>

