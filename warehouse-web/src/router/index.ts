import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: MainLayout,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/Dashboard.vue'),
          meta: { title: '工作台' },
        },
        {
          path: 'users',
          name: 'Users',
          component: () => import('@/views/user/UserList.vue'),
          meta: { title: '用户管理' },
        },
        {
          path: 'goods',
          name: 'Goods',
          component: () => import('@/views/goods/GoodsList.vue'),
          meta: { title: '商品管理' },
        },
        {
          path: 'warehouses',
          name: 'Warehouses',
          component: () => import('@/views/warehouse/WarehouseList.vue'),
          meta: { title: '仓库管理' },
        },
        {
          path: 'stock',
          name: 'Stock',
          component: () => import('@/views/stock/StockList.vue'),
          meta: { title: '库存查询' },
        },
        {
          path: 'inout',
          name: 'Inout',
          component: () => import('@/views/inout/InoutList.vue'),
          meta: { title: '出入库管理' },
        },
        {
          path: 'check',
          name: 'Check',
          component: () => import('@/views/check/CheckList.vue'),
          meta: { title: '库存盘点' },
        },
      ],
    },
  ],
})

export default router
