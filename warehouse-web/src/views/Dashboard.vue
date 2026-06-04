<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { checkApi, goodsApi, inoutApi, stockApi, userApi, warehouseApi } from '@/api'

const loading = ref(false)
const stats = ref([
  { label: '用户总数', value: 0, color: '#2563eb' },
  { label: '商品总数', value: 0, color: '#059669' },
  { label: '仓库总数', value: 0, color: '#d97706' },
  { label: '库存记录', value: 0, color: '#7c3aed' },
  { label: '出入库单', value: 0, color: '#dc2626' },
  { label: '盘点单', value: 0, color: '#0891b2' },
])

async function loadStats() {
  loading.value = true
  try {
    const [users, goods, warehouses, stock, inout, check] = await Promise.all([
      userApi.page({ current: 1, size: 1 }),
      goodsApi.page({ current: 1, size: 1 }),
      warehouseApi.page({ current: 1, size: 1 }),
      stockApi.page({ current: 1, size: 1 }),
      inoutApi.page({ current: 1, size: 1 }),
      checkApi.page({ current: 1, size: 1 }),
    ])
    stats.value[0].value = users.total
    stats.value[1].value = goods.total
    stats.value[2].value = warehouses.total
    stats.value[3].value = stock.total
    stats.value[4].value = inout.total
    stats.value[5].value = check.total
  } catch {
    /* 错误已由 axios 拦截器提示 */
  } finally {
    loading.value = false
  }
}

onMounted(loadStats)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2>工作台</h2>
      <el-button :loading="loading" @click="loadStats">刷新数据</el-button>
    </div>

    <el-row :gutter="16" v-loading="loading">
      <el-col v-for="item in stats" :key="item.label" :xs="24" :sm="12" :md="8" :lg="8">
        <div class="stat-card">
          <div class="stat-value" :style="{ color: item.color }">{{ item.value }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </div>
      </el-col>
    </el-row>

    <el-card class="welcome-card" shadow="never">
      <template #header>
        <span>系统说明</span>
      </template>
      <p>本前端通过 Vite 开发代理连接 Spring Cloud Gateway（<code>http://127.0.0.1:8070</code>）。</p>
      <p>请确保 Nacos、Gateway 及各微服务已启动后再使用各功能模块。</p>
      <el-alert
        type="warning"
        :closable="false"
        show-icon
        title="若出现 401 错误"
        description="8080 端口常被 Sentinel Dashboard 占用。请将 Sentinel 运行在 8858，并启动 warehouse-gateway（8070）。"
        style="margin-bottom: 16px"
      />
      <el-descriptions :column="2" border style="margin-top: 16px">
        <el-descriptions-item label="Gateway">8070</el-descriptions-item>
        <el-descriptions-item label="Sentinel">8858</el-descriptions-item>
        <el-descriptions-item label="用户服务">8081</el-descriptions-item>
        <el-descriptions-item label="商品服务">8082</el-descriptions-item>
        <el-descriptions-item label="仓库服务">8083</el-descriptions-item>
        <el-descriptions-item label="库存服务">8084</el-descriptions-item>
        <el-descriptions-item label="出入库服务">8085</el-descriptions-item>
        <el-descriptions-item label="盘点服务">8086</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<style scoped>
.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 16px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.06);
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  line-height: 1.2;
}

.stat-label {
  margin-top: 8px;
  color: #6b7280;
  font-size: 14px;
}

.welcome-card {
  margin-top: 8px;
}

.welcome-card p {
  margin: 0 0 8px;
  color: #4b5563;
  line-height: 1.6;
}

code {
  background: #f3f4f6;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 13px;
}
</style>
