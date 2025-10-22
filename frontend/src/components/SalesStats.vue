<template>
  <div class="sales-stats">
    <el-card class="header-card">
      <div class="header-section">
        <div>
          <h2>📊 商品销量统计</h2>
          <p>实时查看商品销售情况</p>
        </div>
        <el-button @click="loadData" :loading="loading">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
      </div>
    </el-card>

    <el-card class="table-card">
      <el-table
        :data="tableData"
        v-loading="loading"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="index" label="排名" width="80" align="center" />
        
        <el-table-column prop="productName" label="商品名称" min-width="200">
          <template #default="scope">
            <div class="product-info">
              <strong>{{ scope.row.productName }}</strong>
              <span class="product-id">ID: {{ scope.row.productId }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="currentPrice" label="当前价格" width="120" align="right">
          <template #default="scope">
            <span class="price">¥{{ scope.row.currentPrice }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="totalSoldQuantity" label="总销量" width="120" align="right" sortable>
          <template #default="scope">
            <el-tag type="success" effect="dark">{{ scope.row.totalSoldQuantity }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="totalSalesAmount" label="总销售额" width="150" align="right" sortable>
          <template #default="scope">
            <span class="sales-amount">¥{{ scope.row.totalSalesAmount }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="orderCount" label="订单数" width="100" align="center">
          <template #default="scope">
            <el-tag>{{ scope.row.orderCount }}</el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="currentStock" label="当前库存" width="120" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.currentStock > 50 ? 'success' : scope.row.currentStock > 10 ? 'warning' : 'danger'">
              {{ scope.row.currentStock }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="stockStatus" label="库存状态" width="120" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.stockStatus === 'In Stock' ? 'success' : 'danger'">
              {{ scope.row.stockStatus === 'In Stock' ? '有货' : '缺货' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>

      <!-- 汇总信息 -->
      <el-card class="summary-card" shadow="never">
        <div class="summary-content">
          <div class="summary-item">
            <div class="summary-label">📦 商品总数</div>
            <div class="summary-value">{{ total }}</div>
          </div>
          <div class="summary-item">
            <div class="summary-label">🎯 总销量</div>
            <div class="summary-value">{{ totalSoldQuantity }}</div>
          </div>
          <div class="summary-item">
            <div class="summary-label">💰 总销售额</div>
            <div class="summary-value">¥{{ totalSalesAmount.toFixed(2) }}</div>
          </div>
          <div class="summary-item">
            <div class="summary-label">📋 总订单数</div>
            <div class="summary-value">{{ totalOrderCount }}</div>
          </div>
        </div>
      </el-card>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

interface ProductSalesStats {
  productId: number
  productName: string
  currentPrice: number
  totalSoldQuantity: number
  totalSalesAmount: number
  orderCount: number
  currentStock: number
  stockStatus: string
}

const loading = ref(false)
const tableData = ref<ProductSalesStats[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

// 计算汇总数据
const totalSoldQuantity = computed(() => {
  return tableData.value.reduce((sum, item) => sum + item.totalSoldQuantity, 0)
})

const totalSalesAmount = computed(() => {
  return tableData.value.reduce((sum, item) => sum + item.totalSalesAmount, 0)
})

const totalOrderCount = computed(() => {
  return tableData.value.reduce((sum, item) => sum + item.orderCount, 0)
})

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const response = await axios.get('/stats/product-sales', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    
    if (response.success) {
      tableData.value = response.data.records
      total.value = response.data.total
    } else {
      ElMessage.error(response.message || '加载数据失败')
    }
  } catch (error: any) {
    console.error('加载销量统计失败:', error)
    ElMessage.error(error.response?.data?.message || error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

// 分页处理
const handlePageChange = (page: number) => {
  currentPage.value = page
  loadData()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  loadData()
}

// 页面加载时获取数据
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.sales-stats {
  padding: 20px;
}

.header-card {
  margin-bottom: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-section h2 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
}

.header-section p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.table-card {
  margin-bottom: 20px;
}

.product-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.product-id {
  font-size: 12px;
  color: #909399;
}

.price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}

.sales-amount {
  color: #67c23a;
  font-weight: bold;
  font-size: 16px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.summary-card {
  margin-top: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.summary-content {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.summary-item {
  text-align: center;
  padding: 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  backdrop-filter: blur(10px);
}

.summary-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 10px;
}

.summary-value {
  font-size: 28px;
  font-weight: bold;
  color: #ffffff;
}

@media (max-width: 768px) {
  .summary-content {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>

