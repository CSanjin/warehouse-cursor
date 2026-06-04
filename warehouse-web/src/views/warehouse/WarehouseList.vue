<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { warehouseApi } from '@/api'
import type { WarehouseInfo } from '@/types'

const loading = ref(false)
const tableData = ref<WarehouseInfo[]>([])
const total = ref(0)
const query = reactive({ current: 1, size: 10, warehouseName: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('新增仓库')
const formRef = ref()
const form = reactive<WarehouseInfo>({
  warehouseCode: '',
  warehouseName: '',
  address: '',
  manager: '',
  phone: '',
  status: 1,
})

const rules = {
  warehouseCode: [{ required: true, message: '请输入仓库编码', trigger: 'blur' }],
  warehouseName: [{ required: true, message: '请输入仓库名称', trigger: 'blur' }],
}

async function loadData() {
  loading.value = true
  try {
    const res = await warehouseApi.page({
      current: query.current,
      size: query.size,
      warehouseName: query.warehouseName || undefined,
    })
    tableData.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  query.current = 1
  loadData()
}

function resetForm() {
  Object.assign(form, {
    id: undefined,
    warehouseCode: '',
    warehouseName: '',
    address: '',
    manager: '',
    phone: '',
    status: 1,
  })
}

function handleAdd() {
  resetForm()
  dialogTitle.value = '新增仓库'
  dialogVisible.value = true
}

function handleEdit(row: WarehouseInfo) {
  resetForm()
  Object.assign(form, row)
  dialogTitle.value = '编辑仓库'
  dialogVisible.value = true
}

async function handleDelete(row: WarehouseInfo) {
  await ElMessageBox.confirm(`确定删除仓库「${row.warehouseName}」吗？`, '提示', { type: 'warning' })
  await warehouseApi.remove(row.id!)
  ElMessage.success('删除成功')
  loadData()
}

async function handleSubmit() {
  await formRef.value?.validate()
  if (form.id) {
    await warehouseApi.update(form)
    ElMessage.success('更新成功')
  } else {
    await warehouseApi.create(form)
    ElMessage.success('创建成功')
  }
  dialogVisible.value = false
  loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>仓库管理</h2>
      <el-button type="primary" @click="handleAdd">新增仓库</el-button>
    </div>

    <div class="search-bar">
      <el-input v-model="query.warehouseName" placeholder="仓库名称" clearable style="width: 220px" @keyup.enter="handleSearch" />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <div class="table-card">
      <el-table v-loading="loading" :data="tableData" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="warehouseCode" label="编码" min-width="120" />
        <el-table-column prop="warehouseName" label="名称" min-width="140" />
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip />
        <el-table-column prop="manager" label="负责人" min-width="100" />
        <el-table-column prop="phone" label="联系电话" min-width="130" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrap">
        <el-pagination
          v-model:current-page="query.current"
          v-model:page-size="query.size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @change="loadData"
        />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="编码" prop="warehouseCode">
          <el-input v-model="form.warehouseCode" />
        </el-form-item>
        <el-form-item label="名称" prop="warehouseName">
          <el-input v-model="form.warehouseName" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="form.manager" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
