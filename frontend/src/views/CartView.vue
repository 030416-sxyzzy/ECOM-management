<template>
  <div class="cart">
    <div class="cart-container">
      <h1 class="cart-title">购物车</h1>
      
      <div v-if="loading" class="loading">
        <el-skeleton :rows="5" animated />
      </div>

      <div v-else-if="cartItems.length === 0" class="empty-cart">
        <el-empty description="购物车是空的">
          <el-button type="primary" @click="goToProducts">
            去购物
          </el-button>
        </el-empty>
      </div>

      <div v-else class="cart-content">
        <!-- 购物车商品列表 -->
        <div class="cart-items">
          <div class="cart-header">
            <el-checkbox
              v-model="selectAll"
              @change="handleSelectAll"
            >
              全选
            </el-checkbox>
            <span class="header-text">商品信息</span>
            <span class="header-text">单价</span>
            <span class="header-text">数量</span>
            <span class="header-text">小计</span>
            <span class="header-text">操作</span>
          </div>

          <div
            v-for="item in cartItems"
            :key="item.id"
            class="cart-item"
          >
            <el-checkbox
              v-model="item.selected"
              @change="updateSelectAll"
            />
            
            <div class="item-info">
              <div class="item-image">
                <img :src="item.imageUrl" :alt="item.productName" />
              </div>
              <div class="item-details">
                <h3 class="item-name">{{ item.productName }}</h3>
                <p class="item-specs">商品ID: {{ item.productId }}</p>
                <p class="item-stock" :class="{ 'low-stock': item.productStock < 10 }">
                  库存: {{ item.productStock }} 件
                </p>
              </div>
            </div>

            <div class="item-price">
              <span class="price">¥{{ item.price }}</span>
            </div>

            <div class="item-quantity">
              <el-input-number
                v-model="item.quantity"
                :min="1"
                :max="item.productStock"
                :disabled="item.productStock === 0"
                @change="updateQuantity(item)"
              />
            </div>

            <div class="item-subtotal">
              <span class="subtotal">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
            </div>

            <div class="item-actions">
              <el-button
                type="danger"
                size="small"
                @click="removeItem(item.id)"
              >
                删除
              </el-button>
            </div>
          </div>
        </div>

        <!-- 购物车统计 -->
        <div class="cart-summary">
          <div class="summary-info">
            <p>已选择 {{ selectedCount }} 件商品</p>
            <p>合计: <span class="total-amount">¥{{ totalAmount.toFixed(2) }}</span></p>
          </div>
          
          <div class="summary-actions">
            <el-button @click="clearCart">
              清空购物车
            </el-button>
            <el-button
              type="primary"
              size="large"
              :disabled="selectedCount === 0"
              @click="checkout"
            >
              结算 ({{ selectedCount }})
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 收货信息对话框 -->
    <el-dialog
      v-model="addressDialogVisible"
      title="填写收货信息"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="addressForm" :rules="addressRules" ref="addressFormRef" label-width="100px">
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="receiverPhone">
          <el-input v-model="addressForm.receiverPhone" placeholder="请输入手机号码" maxlength="11" />
        </el-form-item>
        <el-form-item label="收货地址" prop="receiverAddress">
          <el-input
            v-model="addressForm.receiverAddress"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址，如：北京市朝阳区xx街道xx号"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addressDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmCheckout" :loading="checkoutLoading">
            确认下单
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { CartItem } from '@/types'
import { useCartStore } from '@/stores/cart'
import axios from '../utils/axios'

const router = useRouter()
const cartStore = useCartStore()

// 数据
const cartItems = ref<(CartItem & { selected: boolean })[]>([])
const loading = ref(false)
const addressDialogVisible = ref(false)
const checkoutLoading = ref(false)
const addressFormRef = ref()

// 收货信息表单
const addressForm = ref({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: ''
})

// 表单验证规则
const addressRules = {
  receiverName: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  receiverPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  receiverAddress: [
    { required: true, message: '请输入收货地址', trigger: 'blur' },
    { min: 5, max: 200, message: '地址长度在 5 到 200 个字符', trigger: 'blur' }
  ]
}

// 计算属性
const selectAll = ref(false)

const selectedCount = computed(() => {
  return cartItems.value.filter(item => item.selected).length
})

const totalAmount = computed(() => {
  return cartItems.value
    .filter(item => item.selected)
    .reduce((total, item) => total + (item.price * item.quantity), 0)
})

// 生命周期
onMounted(async () => {
  await loadCartItems()
})

// 加载购物车商品
const loadCartItems = async () => {
  loading.value = true
  try {
    await cartStore.fetchCartItems()
    cartItems.value = cartStore.cartItems.map(item => ({
      ...item,
      selected: true // 默认全选
    }))
    updateSelectAll()
  } catch (error) {
    console.error('加载购物车失败:', error)
    ElMessage.error('加载购物车失败')
  } finally {
    loading.value = false
  }
}

// 更新全选状态
const updateSelectAll = () => {
  const totalItems = cartItems.value.length
  const selectedItems = cartItems.value.filter(item => item.selected).length
  selectAll.value = totalItems > 0 && selectedItems === totalItems
}

// 处理全选
const handleSelectAll = (checked: boolean) => {
  cartItems.value.forEach(item => {
    item.selected = checked
  })
}

// 更新商品数量
const updateQuantity = async (item: CartItem & { selected: boolean }) => {
  try {
    const success = await cartStore.updateCartItem(item.id, item.quantity)
    if (!success) {
      ElMessage.error('更新数量失败')
      // 恢复原数量
      await loadCartItems()
    }
  } catch (error) {
    console.error('更新数量失败:', error)
    ElMessage.error('更新数量失败')
  }
}

// 删除商品
const removeItem = async (cartItemId: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const success = await cartStore.deleteCartItem(cartItemId)
    if (success) {
      ElMessage.success('删除成功')
      await loadCartItems()
    } else {
      ElMessage.error('删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除商品失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 清空购物车
const clearCart = async () => {
  try {
    await ElMessageBox.confirm('确定要清空购物车吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const success = await cartStore.clearCart()
    if (success) {
      ElMessage.success('购物车已清空')
      cartItems.value = []
    } else {
      ElMessage.error('清空失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空购物车失败:', error)
      ElMessage.error('清空失败')
    }
  }
}

// 结算 - 显示收货信息对话框
const checkout = () => {
  if (selectedCount.value === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  
  // 清空表单
  addressForm.value = {
    receiverName: '',
    receiverPhone: '',
    receiverAddress: ''
  }
  
  // 显示收货信息对话框
  addressDialogVisible.value = true
}

// 确认下单
const confirmCheckout = async () => {
  // 验证表单
  if (!addressFormRef.value) return
  
  try {
    await addressFormRef.value.validate()
  } catch (error) {
    return
  }
  
  checkoutLoading.value = true
  
  try {
    // 准备订单数据
    const selectedItems = cartItems.value.filter(item => item.selected)
    const orderData = {
      userId: cartStore.userId,
      totalAmount: totalAmount.value,
      orderItems: selectedItems.map(item => ({
        productId: item.productId,
        quantity: item.quantity,
        price: item.price
      })),
      receiverName: addressForm.value.receiverName,
      receiverPhone: addressForm.value.receiverPhone,
      receiverAddress: addressForm.value.receiverAddress,
      shippingAddress: `${addressForm.value.receiverName}, ${addressForm.value.receiverPhone}, ${addressForm.value.receiverAddress}`
    }
    
    // 调用API创建订单
    const response = await axios.post('/orders', orderData)
    
    if (response.code === 200) {
      ElMessage.success(`订单创建成功！订单号: ${response.data}`)
      
      // 关闭对话框
      addressDialogVisible.value = false
      
      // 删除已结算的购物车商品
      for (const item of selectedItems) {
        await cartStore.deleteCartItem(item.id)
      }
      
      // 重新加载购物车
      await loadCartItems()
    } else {
      ElMessage.error(response.message || '创建订单失败')
    }
  } catch (error: any) {
    console.error('结算失败:', error)
    ElMessage.error(error.response?.data?.message || error.message || '结算失败')
  } finally {
    checkoutLoading.value = false
  }
}

// 跳转到首页（商品列表页面）
const goToProducts = () => {
  router.push('/')
}

// 监听购物车变化
watch(() => cartStore.cartItems, () => {
  cartItems.value = cartStore.cartItems.map(item => ({
    ...item,
    selected: item.selected || true
  }))
  updateSelectAll()
}, { deep: true })
</script>

<style scoped>
.cart {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.cart-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.cart-title {
  margin: 0;
  padding: 20px 30px;
  background: #f8f9fa;
  border-bottom: 1px solid #e4e7ed;
  color: #333;
}

.loading {
  padding: 40px;
}

.empty-cart {
  padding: 60px 40px;
  text-align: center;
}

.cart-content {
  padding: 0;
}

.cart-items {
  padding: 0 30px;
}

.cart-header {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #e4e7ed;
  font-weight: bold;
  color: #333;
}

.cart-header .header-text {
  flex: 1;
  text-align: center;
}

.cart-header .header-text:first-of-type {
  text-align: left;
  margin-left: 30px;
}

.cart-item {
  display: flex;
  align-items: center;
  padding: 20px 0;
  border-bottom: 1px solid #f0f0f0;
}

.cart-item:last-child {
  border-bottom: none;
}

.item-info {
  flex: 2;
  display: flex;
  align-items: center;
  margin-left: 20px;
}

.item-image {
  width: 80px;
  height: 80px;
  margin-right: 15px;
  border-radius: 4px;
  overflow: hidden;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-details {
  flex: 1;
}

.item-name {
  margin: 0 0 8px 0;
  color: #333;
  font-size: 1rem;
  line-height: 1.4;
}

.item-specs,
.item-stock {
  margin: 4px 0;
  color: #666;
  font-size: 0.9rem;
}

.item-stock.low-stock {
  color: #f56c6c;
}

.item-price {
  flex: 1;
  text-align: center;
}

.price {
  font-size: 1.1rem;
  font-weight: bold;
  color: #e6a23c;
}

.item-quantity {
  flex: 1;
  text-align: center;
}

.item-subtotal {
  flex: 1;
  text-align: center;
}

.subtotal {
  font-size: 1.1rem;
  font-weight: bold;
  color: #e6a23c;
}

.item-actions {
  flex: 1;
  text-align: center;
}

.cart-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 30px;
  background: #f8f9fa;
  border-top: 1px solid #e4e7ed;
}

.summary-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.summary-info p {
  margin: 0;
  color: #666;
}

.total-amount {
  font-size: 1.5rem;
  font-weight: bold;
  color: #e6a23c;
}

.summary-actions {
  display: flex;
  gap: 15px;
}
</style>

