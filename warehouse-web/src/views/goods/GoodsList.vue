<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { goodsApi } from '@/api'
import type { Goods } from '@/types'

const loading = ref(false)
const tableData = ref<Goods[]>([])
const total = ref(0)
const query = reactive({ current: 1, size: 10, goodsName: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('新增商品')
const formRef = ref()
const form = reactive<Goods>({
  goodsCode: '',
  goodsName: '',
  category: '',
  unit: '',
  price: 0,
  spec: '',
  status: 1,
})

const rules = {
  goodsCode: [{ required: true, message: '请输入商品编码', trigger: 'blur' }],
  goodsName: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
}

async function loadData() {
  loading.value = true
  try {
    const res = await goodsApi.page({
      current: query.current,
      size: query.size,
      goodsName: query.goodsName || undefined,
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
    goodsCode: '',
    goodsName: '',
    category: '',
    unit: '',
    price: 0,
    spec: '',
    status: 1,
  })
}

function handleAdd() {
  resetForm()
  dialogTitle.value = '新增商品'
  dialogVisible.value = true
}

function handleEdit(row: Goods) {
  resetForm()
  Object.assign(form, row)
  dialogTitle.value = '编辑商品'
  dialogVisible.value = true
}

async function handleDelete(row: Goods) {
  await ElMessageBox.confirm(`确定删除商品「${row.goodsName}」吗？`, '提示', { type: 'warning' })
  await goodsApi.remove(row.id!)
  ElMessage.success('删除成功')
  loadData()
}

async function handleSubmit() {
  await formRef.value?.validate()
  if (form.id) {
    await goodsApi.update(form)
    ElMessage.success('更新成功')
  } else {
    await goodsApi.create(form)
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
      <h2>商品管理</h2>
      <el-button type="primary" @click="handleAdd">新增商品</el-button>
    </div>

    <div class="search-bar">
      <el-input v-model="query.goodsName" placeholder="商品名称" clearable style="width: 220px" @keyup.enter="handleSearch" />
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <div class="table-card">
      <el-table v-loading="loading" :data="tableData" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="goodsCode" label="编码" min-width="120" />
        <el-table-column prop="goodsName" label="名称" min-width="140" />
        <el-table-column prop="category" label="分类" min-width="100" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="price" label="单价" width="100">
          <template #default="{ row }">¥ {{ row.price ?? 0 }}</template>
        </el-table-column>
        <el-table-column prop="spec" label="规格" min-width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '上架' : '下架' }}
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
        <el-form-item label="编码" prop="goodsCode">
          <el-input v-model="form.goodsCode" />
        </el-form-item>
        <el-form-item label="名称" prop="goodsName">
          <el-input v-model="form.goodsName" />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="form.category" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit" />
        </el-form-item>
        <el-form-item label="单价">
          <el-input-number v-model="form.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.spec" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">上架</el-radio>
            <el-radio :value="0">下架</el-radio>
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
