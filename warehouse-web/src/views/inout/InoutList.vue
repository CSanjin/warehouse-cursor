<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { inoutApi } from '@/api'
import type { StockInOut } from '@/types'

const loading = ref(false)
const tableData = ref<StockInOut[]>([])
const total = ref(0)
const query = reactive({ current: 1, size: 10, orderType: '' })
const dialogVisible = ref(false)
const dialogTitle = ref('新建出入库单')
const formRef = ref()
const form = reactive<StockInOut>({
  orderType: 'IN',
  warehouseId: 1,
  goodsId: 1,
  quantity: 1,
  operator: 'admin',
  status: 0,
  remark: '',
})

const rules = {
  orderType: [{ required: true, message: '请选择类型', trigger: 'change' }],
  warehouseId: [{ required: true, message: '请输入仓库ID', trigger: 'blur' }],
  goodsId: [{ required: true, message: '请输入商品ID', trigger: 'blur' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
}

const statusMap: Record<number, { label: string; type: 'info' | 'success' | 'danger' }> = {
  0: { label: '待确认', type: 'info' },
  1: { label: '已确认', type: 'success' },
  2: { label: '已取消', type: 'danger' },
}

async function loadData() {
  loading.value = true
  try {
    const res = await inoutApi.page({
      current: query.current,
      size: query.size,
      orderType: query.orderType || undefined,
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
    orderNo: undefined,
    orderType: 'IN',
    warehouseId: 1,
    goodsId: 1,
    quantity: 1,
    operator: 'admin',
    status: 0,
    remark: '',
  })
}

function handleAdd() {
  resetForm()
  dialogTitle.value = '新建出入库单'
  dialogVisible.value = true
}

function handleEdit(row: StockInOut) {
  resetForm()
  Object.assign(form, row)
  dialogTitle.value = '编辑出入库单'
  dialogVisible.value = true
}

async function handleDelete(row: StockInOut) {
  await ElMessageBox.confirm(`确定删除单据「${row.orderNo || row.id}」吗？`, '提示', { type: 'warning' })
  await inoutApi.remove(row.id!)
  ElMessage.success('删除成功')
  loadData()
}

async function handleConfirm(row: StockInOut) {
  await ElMessageBox.confirm('确认后将通过 MQ 异步更新库存，是否继续？', '确认出入库', { type: 'warning' })
  await inoutApi.confirm(row.id!)
  ElMessage.success('确认成功')
  loadData()
}

async function handleSubmit() {
  await formRef.value?.validate()
  if (form.id) {
    await inoutApi.update(form)
    ElMessage.success('更新成功')
  } else {
    await inoutApi.create(form)
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
      <h2>出入库管理</h2>
      <el-button type="primary" @click="handleAdd">新建单据</el-button>
    </div>

    <div class="search-bar">
      <el-select v-model="query.orderType" placeholder="单据类型" clearable style="width: 160px">
        <el-option label="入库" value="IN" />
        <el-option label="出库" value="OUT" />
      </el-select>
      <el-button type="primary" @click="handleSearch">查询</el-button>
    </div>

    <div class="table-card">
      <el-table v-loading="loading" :data="tableData" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="orderNo" label="单号" min-width="140" />
        <el-table-column prop="orderType" label="类型" width="90">
          <template #default="{ row }">
            <el-tag :type="row.orderType === 'IN' ? 'success' : 'warning'">
              {{ row.orderType === 'IN' ? '入库' : '出库' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warehouseId" label="仓库ID" width="90" />
        <el-table-column prop="goodsId" label="商品ID" width="90" />
        <el-table-column prop="quantity" label="数量" width="90" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status ?? 0]?.type || 'info'">
              {{ statusMap[row.status ?? 0]?.label || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" link type="success" @click="handleConfirm(row)">确认</el-button>
            <el-button v-if="row.status === 0" link type="primary" @click="handleEdit(row)">编辑</el-button>
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
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="类型" prop="orderType">
          <el-radio-group v-model="form.orderType">
            <el-radio value="IN">入库</el-radio>
            <el-radio value="OUT">出库</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="仓库ID" prop="warehouseId">
          <el-input-number v-model="form.warehouseId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="商品ID" prop="goodsId">
          <el-input-number v-model="form.goodsId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number v-model="form.quantity" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="操作人">
          <el-input v-model="form.operator" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
