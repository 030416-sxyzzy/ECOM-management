<template>
  <div class="order-management">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-input
        v-model="filters.orderId"
        placeholder="订单号"
        style="width: 200px"
        clearable
        @clear="loadData"
      />
      <el-select
        v-model="filters.status"
        placeholder="订单状态"
        style="width: 150px"
        clearable
        @clear="loadData"
      >
        <el-option label="待支付" value="pending" />
        <el-option label="已支付" value="paid" />
        <el-option label="已发货" value="shipped" />
        <el-option label="已完成" value="completed" />
        <el-option label="已取消" value="cancelled" />
      </el-select>
      <el-button type="primary" @click="loadData">搜索</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" v-loading="loading" border stripe>
      <el-table-column prop="id" label="订单ID" width="100" />
      <el-table-column prop="userId" label="用户ID" width="100" />
      <el-table-column prop="totalAmount" label="总金额" width="120">
        <template #default="{ row }">
          ¥{{ row.totalAmount }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="receiverName" label="收货人" width="120" />
      <el-table-column prop="receiverPhone" label="联系电话" width="130" />
      <el-table-column prop="receiverAddress" label="收货地址" min-width="200" show-overflow-tooltip />
      <el-table-column prop="createdAt" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="viewDetail(row)">查看详情</el-button>
          <el-button
            v-if="row.status === 'pending'"
            type="warning"
            size="small"
            @click="updateStatus(row.id, 'paid')"
          >
            标记已支付
          </el-button>
          <el-button
            v-if="row.status === 'paid'"
            type="success"
            size="small"
            @click="updateStatus(row.id, 'shipped')"
          >
            标记已发货
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="page"
      v-model:page-size="size"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next"
      @current-change="loadData"
      @size-change="loadData"
      style="margin-top: 20px; justify-content: center"
    />

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="订单详情"
      width="800px"
    >
      <div v-if="currentOrder">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ currentOrder.id }}</el-descriptions-item>
          <el-descriptions-item label="用户ID">{{ currentOrder.userId }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(currentOrder.status)">
              {{ getStatusText(currentOrder.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="总金额">¥{{ currentOrder.totalAmount }}</el-descriptions-item>
          <el-descriptions-item label="收货人">{{ currentOrder.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ currentOrder.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item label="收货地址" :span="2">{{ currentOrder.receiverAddress }}</el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">{{ currentOrder.createdAt }}</el-descriptions-item>
        </el-descriptions>

        <h3 style="margin-top: 20px; margin-bottom: 10px">订单商品</h3>
        <el-table :data="currentOrder.items" border>
          <el-table-column prop="productId" label="商品ID" width="100" />
          <el-table-column prop="productName" label="商品名称" min-width="150" />
          <el-table-column prop="price" label="单价" width="120">
            <template #default="{ row }">
              ¥{{ row.price }}
            </template>
          </el-table-column>
          <el-table-column prop="quantity" label="数量" width="100" />
          <el-table-column label="小计" width="120">
            <template #default="{ row }">
              ¥{{ (row.price * row.quantity).toFixed(2) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const filters = reactive({ orderId: '', status: '' })

const detailVisible = ref(false)
const currentOrder = ref(null)

// 加载订单列表
const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/orders', {
      params: {
        orderId: filters.orderId,
        status: filters.status,
        page: page.value,
        size: size.value
      }
    })
    
    // axios拦截器已经返回了response.data，所以res是Result对象
    // res.data就是订单列表
    if (res.data) {
      tableData.value = Array.isArray(res.data) ? res.data : []
      total.value = Array.isArray(res.data) ? res.data.length : 0
    } else {
      tableData.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('加载订单失败:', error)
    ElMessage.error('加载订单失败')
  } finally {
    loading.value = false
  }
}

// 查看订单详情
const viewDetail = async (row: any) => {
  try {
    const res = await axios.get(`/orders/${row.id}`)
    currentOrder.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error('加载订单详情失败:', error)
    ElMessage.error('加载订单详情失败')
  }
}

// 更新订单状态
const updateStatus = async (orderId: number, newStatus: string) => {
  try {
    await axios.put(`/orders/${orderId}/status`, null, {
      params: { status: newStatus }
    })
    ElMessage.success('状态更新成功')
    loadData()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '状态更新失败')
  }
}

// 获取状态类型
const getStatusType = (status: string) => {
  const typeMap: any = {
    pending: 'warning',
    paid: 'primary',
    shipped: 'info',
    completed: 'success',
    cancelled: 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status: string) => {
  const textMap: any = {
    pending: '待支付',
    paid: '已支付',
    shipped: '已发货',
    completed: '已完成',
    cancelled: '已取消'
  }
  return textMap[status] || status
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.order-management {
  padding: 0;
}

.toolbar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}

:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-pagination) {
  display: flex;
}
</style>

