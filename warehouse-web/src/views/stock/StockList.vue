<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { stockApi } from '@/api'
import type { StockRecord } from '@/types'

const loading = ref(false)
const tableData = ref<StockRecord[]>([])
const total = ref(0)
const query = reactive<{ current: number; size: number; warehouseId?: number; goodsId?: number }>({
  current: 1,
  size: 10,
  warehouseId: undefined,
  goodsId: undefined,
})

async function loadData() {
  loading.value = true
  try {
    const res = await stockApi.page({
      current: query.current,
      size: query.size,
      warehouseId: query.warehouseId,
      goodsId: query.goodsId,
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

function handleReset() {
  query.warehouseId = undefined
  query.goodsId = undefined
  query.current = 1
  loadData()
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>库存查询</h2>
    </div>

    <div class="search-bar">
      <el-input-number v-model="query.warehouseId" placeholder="仓库ID" :min="1" controls-position="right" />
      <el-input-number v-model="query.goodsId" placeholder="商品ID" :min="1" controls-position="right" />
      <el-button type="primary" @click="handleSearch">查询</el-button>
      <el-button @click="handleReset">重置</el-button>
    </div>

    <div class="table-card">
      <el-table v-loading="loading" :data="tableData" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="warehouseId" label="仓库ID" width="100" />
        <el-table-column prop="goodsId" label="商品ID" width="100" />
        <el-table-column prop="quantity" label="库存数量" width="120" />
        <el-table-column prop="lockQuantity" label="锁定数量" width="120" />
        <el-table-column prop="remark" label="备注" min-width="160" show-overflow-tooltip />
        <el-table-column prop="updateTime" label="更新时间" min-width="170" />
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
  </div>
</template>
