# 仓储管理系统 - 前端

基于 **Vue 3 + Vite + TypeScript + Element Plus** 的仓储管理前端，对接 Spring Cloud Gateway（8070）。

## 功能模块

| 模块 | 路由 | 后端 API |
|------|------|----------|
| 工作台 | `/dashboard` | 汇总各服务分页总数 |
| 用户管理 | `/users` | `/api/user/users/**` |
| 商品管理 | `/goods` | `/api/goods/**` |
| 仓库管理 | `/warehouses` | `/api/base/warehouses/**` |
| 库存查询 | `/stock` | `/api/stock/**` |
| 出入库管理 | `/inout` | `/api/inout/**` |
| 库存盘点 | `/check` | `/api/check/**` |

## 启动

1. 确保后端 Gateway（8070）及各微服务已启动
2. 安装依赖并启动开发服务器：

```bash
cd warehouse-web
npm install
npm run dev
```

3. 浏览器访问 http://localhost:5173

开发模式下 Vite 会将 `/api` 代理到 `http://127.0.0.1:8070`（可在 `.env.development` 中修改 `VITE_API_PROXY_TARGET`）。

## 常见问题

**401 Unauthorized**：通常是前端代理到了错误端口。8080 默认为 Sentinel Dashboard 端口，Gateway 应运行在 **8070**。请确认：

1. Sentinel Dashboard 运行在 **8858**（不要用 8080）
2. `warehouse-gateway` 已启动（**8070**）
3. Nacos 及各微服务已启动
4. 修改 `.env.development` 后需重启 `npm run dev`

## 构建

```bash
npm run build
npm run preview
```

## 技术栈

- Vue 3 (Composition API)
- Vue Router 4
- Element Plus
- Axios
- TypeScript
- Vite 7
