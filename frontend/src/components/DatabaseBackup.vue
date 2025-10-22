<template>
  <div class="database-backup">
    <el-card class="header-card">
      <div class="header-section">
        <div>
          <h2>💾 数据库备份</h2>
          <p>定期备份数据，确保数据安全</p>
        </div>
        <div class="header-actions">
          <el-button type="primary" @click="showBackupDialog = true" :loading="backupLoading">
            <el-icon><DocumentAdd /></el-icon>
            立即备份
          </el-button>
          <el-button @click="loadData" :loading="loading">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
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
        <el-table-column type="index" label="#" width="60" align="center" />
        
        <el-table-column prop="backupFileName" label="备份文件" min-width="250">
          <template #default="scope">
            <div class="file-info">
              <el-icon><Document /></el-icon>
              <span class="file-name">{{ scope.row.backupFileName }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="backupFileSize" label="文件大小" width="120" align="right">
          <template #default="scope">
            <span>{{ formatFileSize(scope.row.backupFileSize) }}</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="backupType" label="备份类型" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.backupType === 'manual' ? 'primary' : 'success'">
              {{ scope.row.backupType === 'manual' ? '手动' : '自动' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="备份状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="description" label="备份说明" min-width="150" show-overflow-tooltip />
        
        <el-table-column prop="createdAt" label="备份时间" width="180" align="center">
          <template #default="scope">
            {{ formatDateTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="scope">
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row)"
              :disabled="scope.row.status === 'in_progress'"
            >
              删除
            </el-button>
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
    </el-card>

    <!-- 备份对话框 -->
    <el-dialog
      v-model="showBackupDialog"
      title="数据库备份"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="backupForm" label-width="100px">
        <el-form-item label="备份说明">
          <el-input
            v-model="backupForm.description"
            placeholder="请输入备份说明（可选）"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        <el-alert
          title="提示"
          type="info"
          :closable="false"
          style="margin-bottom: 20px"
        >
          <p>备份操作将创建数据库的完整副本</p>
          <p>备份文件将保存在 database/backups/ 目录下</p>
          <p>建议定期备份数据库以防止数据丢失</p>
        </el-alert>
      </el-form>

      <template #footer>
        <el-button @click="showBackupDialog = false">取消</el-button>
        <el-button type="primary" @click="performBackup" :loading="backupLoading">
          开始备份
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from '../utils/axios'

interface BackupRecord {
  id: number
  backupFileName: string
  backupFilePath: string
  backupFileSize: number
  backupType: string
  status: string
  description: string
  createdAt: string
}

const loading = ref(false)
const backupLoading = ref(false)
const showBackupDialog = ref(false)
const tableData = ref<BackupRecord[]>([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

const backupForm = ref({
  description: ''
})

// 加载备份记录
const loadData = async () => {
  loading.value = true
  try {
    const response = await axios.get('/backup/records', {
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
    console.error('加载备份记录失败:', error)
    ElMessage.error(error.response?.data?.message || error.message || '加载数据失败')
  } finally {
    loading.value = false
  }
}

// 执行备份
const performBackup = async () => {
  backupLoading.value = true
  try {
    const response = await axios.post('/backup/perform', {
      description: backupForm.value.description || '手动备份'
    })
    
    if (response.success) {
      ElMessage.success('数据库备份成功！')
      showBackupDialog.value = false
      backupForm.value.description = ''
      // 重新加载列表
      await loadData()
    } else {
      ElMessage.error(response.message || '备份失败')
    }
  } catch (error: any) {
    console.error('数据库备份失败:', error)
    ElMessage.error(error.response?.data?.message || error.message || '备份失败')
  } finally {
    backupLoading.value = false
  }
}

// 删除备份记录
const handleDelete = async (row: BackupRecord) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除备份记录 "${row.backupFileName}" 吗？此操作将同时删除备份文件，且不可恢复！`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await axios.delete(`/backup/records/${row.id}`)
    
    if (response.success) {
      ElMessage.success('删除成功')
      await loadData()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error: any) {
    if (error !== 'cancel') {
      console.error('删除备份记录失败:', error)
      ElMessage.error(error.response?.data?.message || error.message || '删除失败')
    }
  }
}

// 格式化文件大小
const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 B'
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB'
  if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)).toFixed(2) + ' MB'
  return (bytes / (1024 * 1024 * 1024)).toFixed(2) + ' GB'
}

// 格式化日期时间
const formatDateTime = (dateTime: string): string => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 获取状态类型
const getStatusType = (status: string): string => {
  switch (status) {
    case 'success':
      return 'success'
    case 'failed':
      return 'danger'
    case 'in_progress':
      return 'warning'
    default:
      return 'info'
  }
}

// 获取状态文本
const getStatusText = (status: string): string => {
  switch (status) {
    case 'success':
      return '成功'
    case 'failed':
      return '失败'
    case 'in_progress':
      return '进行中'
    default:
      return '未知'
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
.database-backup {
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

.header-actions {
  display: flex;
  gap: 10px;
}

.table-card {
  margin-bottom: 20px;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.file-name {
  font-family: 'Courier New', monospace;
  color: #409eff;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>

