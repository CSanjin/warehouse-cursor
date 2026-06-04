<script setup lang="ts">
import { onMounted, reactive, ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { checkApi } from '@/api'
import type { StockCheck } from '@/types'

const loading = ref(false)
const tableData = ref<StockCheck[]>([])
const total = ref(0)
const query = reactive({ current: 1, size: 10 })
const dialogVisible = ref(false)
const dialogTitle = ref('新建盘点单')
const formRef = ref()
const form = reactive<StockCheck>({
  warehouseId: 1,
  goodsId: 1,
  bookQuantity: 0,
  actualQuantity: 0,
  diffQuantity: 0,
  operator: 'admin',
  status: 0,
  remark: '',
})

const rules = {
  warehouseId: [{ required: true, message: '请输入仓库ID', trigger: 'blur' }],
  goodsId: [{ required: true, message: '请输入商品ID', trigger: 'blur' }],
  bookQuantity: [{ required: true, message: '请输入账面数量', trigger: 'blur' }],
  actualQuantity: [{ required: true, message: '请输入实盘数量', trigger: 'blur' }],
}

watch(
  () => [form.bookQuantity, form.actualQuantity],
  () => {
    form.diffQuantity = (form.actualQuantity ?? 0) - (form.bookQuantity ?? 0)
  },
)

async function loadData() {
  loading.value = true
  try {
    const res = await checkApi.page({ current: query.current, size: query.size })
    tableData.value = res.records
    total.value = res.total
  } finally {
    loading.value = false
  }
}

function resetForm() {
  Object.assign(form, {
    id: undefined,
    checkNo: undefined,
    warehouseId: 1,
    goodsId: 1,
    bookQuantity: 0,
    actualQuantity: 0,
    diffQuantity: 0,
    operator: 'admin',
    status: 0,
    remark: '',
  })
}

function handleAdd() {
  resetForm()
  dialogTitle.value = '新建盘点单'
  dialogVisible.value = true
}

function handleEdit(row: StockCheck) {
  resetForm()
  Object.assign(form, row)
  dialogTitle.value = '编辑盘点单'
  dialogVisible.value = true
}

async function handleDelete(row: StockCheck) {
  await ElMessageBox.confirm(`确定删除盘点单「${row.checkNo || row.id}」吗？`, '提示', { type: 'warning' })
  await checkApi.remove(row.id!)
  ElMessage.success('删除成功')
  loadData()
}

async function handleConfirm(row: StockCheck) {
  await ElMessageBox.confirm('确认盘点后将调整库存，是否继续？', '确认盘点', { type: 'warning' })
  try {
    await checkApi.confirm(row.id!)
    ElMessage.success('确认成功')
    loadData()
  } catch {
    /* 后端 confirm 接口当前有测试异常，错误已由拦截器提示 */
  }
}

async function handleSubmit() {
  await formRef.value?.validate()
  form.diffQuantity = form.actualQuantity - form.bookQuantity
  if (form.id) {
    await checkApi.update(form)
    ElMessage.success('更新成功')
  } else {
    await checkApi.create(form)
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
      <h2>库存盘点</h2>
      <el-button type="primary" @click="handleAdd">新建盘点</el-button>
    </div>

    <div class="table-card">
      <el-table v-loading="loading" :data="tableData" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="checkNo" label="盘点单号" min-width="140" />
        <el-table-column prop="warehouseId" label="仓库ID" width="90" />
        <el-table-column prop="goodsId" label="商品ID" width="90" />
        <el-table-column prop="bookQuantity" label="账面数量" width="100" />
        <el-table-column prop="actualQuantity" label="实盘数量" width="100" />
        <el-table-column prop="diffQuantity" label="差异" width="90">
          <template #default="{ row }">
            <span :style="{ color: row.diffQuantity > 0 ? '#059669' : row.diffQuantity < 0 ? '#dc2626' : '#6b7280' }">
              {{ row.diffQuantity > 0 ? '+' : '' }}{{ row.diffQuantity ?? 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '已确认' : '待确认' }}
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
        <el-form-item label="仓库ID" prop="warehouseId">
          <el-input-number v-model="form.warehouseId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="商品ID" prop="goodsId">
          <el-input-number v-model="form.goodsId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="账面数量" prop="bookQuantity">
          <el-input-number v-model="form.bookQuantity" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="实盘数量" prop="actualQuantity">
          <el-input-number v-model="form.actualQuantity" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="差异数量">
          <el-input-number v-model="form.diffQuantity" disabled style="width: 100%" />
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
