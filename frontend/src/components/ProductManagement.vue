<template>
  <div class="product-management">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-input
        v-model="filters.keyword"
        placeholder="搜索商品名称"
        style="width: 200px"
        clearable
        @clear="loadData"
      />
      <el-select
        v-model="filters.categoryId"
        placeholder="选择分类"
        style="width: 150px"
        clearable
        @clear="loadData"
      >
        <el-option
          v-for="cat in categories"
          :key="cat.id"
          :label="cat.name"
          :value="cat.id"
        />
      </el-select>
      <el-select
        v-model="filters.status"
        placeholder="商品状态"
        style="width: 120px"
        clearable
        @clear="loadData"
      >
        <el-option label="上架" value="active" />
        <el-option label="下架" value="inactive" />
      </el-select>
      <el-button type="primary" @click="loadData">搜索</el-button>
      <el-button type="success" @click="openAdd">新增商品</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" v-loading="loading" border stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="商品名称" min-width="150" />
      <el-table-column prop="categoryId" label="分类" width="120">
        <template #default="{ row }">
          {{ getCategoryName(row.categoryId) }}
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">
          ¥{{ row.price }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 'active' ? 'success' : 'info'">
            {{ row.status === 'active' ? '上架' : '下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
          <el-popconfirm title="确定删除?" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="editing ? '编辑商品' : '新增商品'"
      width="600px"
    >
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input v-model="form.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" style="width: 100%">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="图片URL">
          <el-input v-model="form.imageUrl" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status">
            <el-option label="上架" value="active" />
            <el-option label="下架" value="inactive" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

const categories = ref([])
const tableData = ref([])
const total = ref(0)
const loading = ref(false)
const page = ref(1)
const size = ref(10)
const filters = reactive({ keyword: '', categoryId: null, status: '' })

const dialogVisible = ref(false)
const editing = ref(false)
const form = reactive({
  id: null,
  name: '',
  description: '',
  price: 0,
  stock: 0,
  categoryId: null,
  imageUrl: '',
  status: 'active'
})

// 加载分类列表
const loadCategories = async () => {
  try {
    const res = await axios.get('/products/categories')
    // axios拦截器已经返回了response.data
    categories.value = res.data || []
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

// 获取分类名称
const getCategoryName = (id: number) => {
  const cat = categories.value.find((c: any) => c.id === id)
  return cat ? cat.name : '-'
}

// 加载商品列表
const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/products', {
      params: {
        keyword: filters.keyword,
        categoryId: filters.categoryId,
        status: filters.status,
        page: page.value,
        size: size.value
      }
    })
    // axios拦截器已经返回了response.data，所以res就是PageResult对象
    tableData.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('加载商品失败:', error)
    ElMessage.error('加载商品失败')
  } finally {
    loading.value = false
  }
}

// 打开新增对话框
const openAdd = () => {
  editing.value = false
  Object.assign(form, {
    id: null,
    name: '',
    description: '',
    price: 0,
    stock: 0,
    categoryId: null,
    imageUrl: '',
    status: 'active'
  })
  dialogVisible.value = true
}

// 打开编辑对话框
const openEdit = (row: any) => {
  editing.value = true
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

// 保存商品
const handleSave = async () => {
  try {
    if (editing.value) {
      await axios.put(`/products/${form.id}`, form)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/products', form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '保存失败')
  }
}

// 删除商品
const handleDelete = async (id: number) => {
  try {
    await axios.delete(`/products/${id}`)
    ElMessage.success('删除成功')
    loadData()
  } catch (error: any) {
    ElMessage.error(error.response?.data?.message || '删除失败')
  }
}

onMounted(() => {
  loadCategories()
  loadData()
})
</script>

<style scoped>
.product-management {
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

