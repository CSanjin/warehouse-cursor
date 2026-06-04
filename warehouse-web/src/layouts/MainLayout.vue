<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import {
  Box,
  DataBoard,
  DocumentChecked,
  ShoppingCart,
  House,
  Sort,
  User,
} from '@element-plus/icons-vue'

const route = useRoute()

const activeMenu = computed(() => route.path)

const menuItems = [
  { path: '/dashboard', title: '工作台', icon: DataBoard },
  { path: '/users', title: '用户管理', icon: User },
  { path: '/goods', title: '商品管理', icon: ShoppingCart },
  { path: '/warehouses', title: '仓库管理', icon: House },
  { path: '/stock', title: '库存查询', icon: Box },
  { path: '/inout', title: '出入库管理', icon: Sort },
  { path: '/check', title: '库存盘点', icon: DocumentChecked },
]

const pageTitle = computed(() => (route.meta.title as string) || '仓储管理系统')
</script>

<template>
  <el-container class="layout">
    <el-aside width="var(--sidebar-width)" class="sidebar">
      <div class="logo">
        <el-icon :size="22"><Box /></el-icon>
        <span>仓储管理系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1e293b"
        text-color="#cbd5e1"
        active-text-color="#ffffff"
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <h1>{{ pageTitle }}</h1>
        </div>
        <div class="header-right">
          <el-tag type="info" effect="plain">Gateway :8070</el-tag>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout {
  height: 100vh;
}

.sidebar {
  background: #1e293b;
  overflow: hidden;
}

.logo {
  height: var(--header-height);
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 0 20px;
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.sidebar :deep(.el-menu) {
  border-right: none;
}

.sidebar :deep(.el-menu-item.is-active) {
  background: rgba(37, 99, 235, 0.35) !important;
}

.header {
  height: var(--header-height);
  background: #fff;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.header-left h1 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #111827;
}

.main {
  padding: 0;
  background: var(--bg-color);
  overflow: auto;
}
</style>
