<template>
  <div class="product-card" @click="$emit('click', product)">
    <div class="product-image">
      <img :src="product.imageUrl" :alt="product.name" />
      <div v-if="product.stock === 0" class="out-of-stock">
        缺货
      </div>
    </div>
    
    <div class="product-info">
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-description">{{ product.description }}</p>
      <div class="product-price">¥{{ product.price }}</div>
      <div class="product-stock" :class="{ 'low-stock': product.stock < 10 }">
        库存: {{ product.stock }}
      </div>
      
      <div class="product-actions">
        <el-button
          type="primary"
          size="small"
          :disabled="product.stock === 0"
          @click.stop="$emit('add-to-cart', product)"
        >
          加入购物车
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Product } from '@/types'

defineProps<{
  product: Product
}>()

defineEmits<{
  click: [product: Product]
  'add-to-cart': [product: Product]
}>()
</script>

<style scoped>
.product-card {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.product-image {
  height: 200px;
  overflow: hidden;
  position: relative;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.out-of-stock {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 8px 16px;
  border-radius: 4px;
  font-weight: bold;
}

.product-info {
  padding: 15px;
}

.product-name {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 1rem;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
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
  color: #67c23a;
  font-size: 0.9rem;
  margin: 0 0 15px 0;
}

.product-stock.low-stock {
  color: #f56c6c;
}

.product-actions {
  display: flex;
  justify-content: center;
}
</style>

