import axios from 'axios'
import type { AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import type { Result } from '@/types'

const request = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

function resolveErrorMessage(error: unknown): string {
  if (!axios.isAxiosError(error)) {
    return '网络异常，请稍后重试'
  }

  const status = error.response?.status
  const body = error.response?.data as Result | undefined

  if (status === 401) {
    return (
      'API 返回 401：请确认 warehouse-gateway 已启动（端口 8070）。' +
      '若 8080 端口被 Sentinel Dashboard 占用，请将 Sentinel 改到 8858 端口运行。'
    )
  }

  if (status === 404) {
    return '接口不存在(404)：请确认 Gateway 路由配置正确且对应微服务已注册到 Nacos。'
  }

  if (status === 503 || status === 502) {
    return '服务不可用：请确认 Nacos、Gateway 及各微服务均已启动。'
  }

  if (body?.message) {
    return body.message
  }

  return error.message || '网络异常，请稍后重试'
}

request.interceptors.response.use(
  (response: AxiosResponse<Result>) => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return response
  },
  (error) => {
    ElMessage.error(resolveErrorMessage(error))
    return Promise.reject(error)
  },
)

export async function getData<T>(url: string, params?: Record<string, unknown>) {
  const res = await request.get<Result<T>>(url, { params })
  return res.data.data
}

export async function postData<T>(url: string, data?: unknown) {
  const res = await request.post<Result<T>>(url, data)
  return res.data.data
}

export async function putData<T>(url: string, data?: unknown) {
  const res = await request.put<Result<T>>(url, data)
  return res.data.data
}

export async function deleteData<T>(url: string) {
  const res = await request.delete<Result<T>>(url)
  return res.data.data
}

export default request
