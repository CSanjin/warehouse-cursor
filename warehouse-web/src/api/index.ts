import { deleteData, getData, postData, putData } from './request'
import type {
  Goods,
  PageParams,
  PageResult,
  StockCheck,
  StockInOut,
  StockRecord,
  SysUser,
  WarehouseInfo,
} from '@/types'

export const userApi = {
  page: (params: PageParams & { username?: string }) =>
    getData<PageResult<SysUser>>('/user/users/page', params),
  getById: (id: number) => getData<SysUser>(`/user/users/${id}`),
  create: (data: SysUser) => postData<boolean>('/user/users', data),
  update: (data: SysUser) => putData<boolean>('/user/users', data),
  remove: (id: number) => deleteData<boolean>(`/user/users/${id}`),
}

export const goodsApi = {
  page: (params: PageParams & { goodsName?: string }) =>
    getData<PageResult<Goods>>('/goods/page', params),
  getById: (id: number) => getData<Goods>(`/goods/${id}`),
  create: (data: Goods) => postData<boolean>('/goods', data),
  update: (data: Goods) => putData<boolean>('/goods', data),
  remove: (id: number) => deleteData<boolean>(`/goods/${id}`),
}

export const warehouseApi = {
  page: (params: PageParams & { warehouseName?: string }) =>
    getData<PageResult<WarehouseInfo>>('/base/warehouses/page', params),
  getById: (id: number) => getData<WarehouseInfo>(`/base/warehouses/${id}`),
  create: (data: WarehouseInfo) => postData<boolean>('/base/warehouses', data),
  update: (data: WarehouseInfo) => putData<boolean>('/base/warehouses', data),
  remove: (id: number) => deleteData<boolean>(`/base/warehouses/${id}`),
}

export const stockApi = {
  page: (params: PageParams & { warehouseId?: number; goodsId?: number }) =>
    getData<PageResult<StockRecord>>('/stock/page', params),
  getById: (id: number) => getData<StockRecord>(`/stock/${id}`),
  query: (warehouseId: number, goodsId: number) =>
    getData<StockRecord>('/stock/query', { warehouseId, goodsId }),
}

export const inoutApi = {
  page: (params: PageParams & { orderType?: string }) =>
    getData<PageResult<StockInOut>>('/inout/page', params),
  getById: (id: number) => getData<StockInOut>(`/inout/${id}`),
  create: (data: StockInOut) => postData<boolean>('/inout', data),
  update: (data: StockInOut) => putData<boolean>('/inout', data),
  remove: (id: number) => deleteData<boolean>(`/inout/${id}`),
  confirm: (id: number) => postData<boolean>(`/inout/confirm/${id}`),
}

export const checkApi = {
  page: (params: PageParams) => getData<PageResult<StockCheck>>('/check/page', params),
  getById: (id: number) => getData<StockCheck>(`/check/${id}`),
  create: (data: StockCheck) => postData<boolean>('/check', data),
  update: (data: StockCheck) => putData<boolean>('/check', data),
  remove: (id: number) => deleteData<boolean>(`/check/${id}`),
  confirm: (id: number) => postData<boolean>(`/check/confirm/${id}`),
}
