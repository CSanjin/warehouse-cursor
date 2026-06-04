export interface Result<T = unknown> {
  code: number
  message: string
  data: T
}

export interface PageResult<T> {
  total: number
  current: number
  size: number
  records: T[]
}

export interface BaseEntity {
  id?: number
  createTime?: string
  updateTime?: string
  createBy?: string
  updateBy?: string
  deleted?: number
}

export interface SysUser extends BaseEntity {
  username: string
  password?: string
  realName?: string
  phone?: string
  email?: string
  status?: number
}

export interface Goods extends BaseEntity {
  goodsCode: string
  goodsName: string
  category?: string
  unit?: string
  price?: number
  spec?: string
  status?: number
}

export interface WarehouseInfo extends BaseEntity {
  warehouseCode: string
  warehouseName: string
  address?: string
  manager?: string
  phone?: string
  status?: number
}

export interface StockRecord extends BaseEntity {
  warehouseId: number
  goodsId: number
  quantity: number
  lockQuantity?: number
  remark?: string
}

export interface StockInOut extends BaseEntity {
  orderNo?: string
  orderType: string
  warehouseId: number
  goodsId: number
  quantity: number
  operator?: string
  status?: number
  remark?: string
}

export interface StockCheck extends BaseEntity {
  checkNo?: string
  warehouseId: number
  goodsId: number
  bookQuantity: number
  actualQuantity: number
  diffQuantity?: number
  operator?: string
  status?: number
  remark?: string
}

export interface PageParams {
  current?: number
  size?: number
  [key: string]: unknown
}
