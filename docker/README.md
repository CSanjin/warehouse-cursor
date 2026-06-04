# Docker 容器化与集群部署说明

本目录包含仓储微服务系统的 **Docker Compose 集群** 配置，满足课程设计中「Web 应用 + 数据库 + 数据卷 + 自定义网络」的要求。

## 一、容器架构

| 类型 | 容器 | 说明 |
|------|------|------|
| **Web 应用** | `warehouse-web` | Vue3 前端 + Nginx（对外端口 80） |
| **Web 后端** | `warehouse-gateway` | Spring Cloud Gateway（8070） |
| **数据库** | `warehouse-mysql` | MySQL 8.0，6 个业务库 |
| 微服务 | `warehouse-user` 等 6 个 | 用户/商品/仓库/库存/出入库/盘点 |
| 中间件 | `nacos`、`rocketmq-*`、`sentinel` | 注册中心、消息队列、流控 |

**课程最低要求（Web + DB）**：`warehouse-web` + `warehouse-mysql` 两个核心容器；完整运行需依赖网关与微服务集群。

## 二、数据安全与数据卷设计

| 卷名 | 类型 | 用途 |
|------|------|------|
| `warehouse-mysql-data` | 命名卷 | MySQL 数据文件持久化（`/var/lib/mysql`） |
| `warehouse-mysql-logs` | 命名卷 | binlog / 日志，支持数据恢复演示 |
| `warehouse-nacos-data` | 命名卷 | Nacos 配置与注册数据 |
| `warehouse-rocketmq-broker-store` | 命名卷 | MQ 消息持久化 |
| `./sql/init.sql` | **只读** bind mount | 首次启动初始化库表，容器内不可改 |
| `./docker/mysql/conf.d` | **只读** bind mount | MySQL 安全与字符集配置 |

**安全措施：**

1. 数据库密码通过 `.env` 注入，**不写入镜像**
2. MySQL **默认不映射** 3306 到宿主机，仅 `warehouse-net` 内网访问
3. 初始化 SQL **只读挂载**（`:ro`）
4. Java 容器以非 root 用户 `warehouse` 运行
5. `custom.cnf` 开启 binlog，便于演示备份恢复

## 三、自定义网络设计

```yaml
networks:
  warehouse-net:
    driver: bridge
    ipam:
      config:
        - subnet: 172.28.0.0/16
          gateway: 172.28.0.1
```

| 容器 | 固定 IP（示例） | 角色 |
|------|----------------|------|
| mysql | 172.28.0.10 | 数据层 |
| nacos | 172.28.0.11 | 注册中心 |
| gateway | 172.28.0.20 | API 入口 |
| warehouse-web | 172.28.0.21 | Web 入口 |

服务间通过 **容器名** 通信（如 `jdbc:mysql://mysql:3306/...`），与宿主机 IP 解耦。

## 四、快速启动

### 前置条件

- Docker Desktop 20.10+
- Docker Compose v2
- 至少 **8GB** 可用内存（首次构建需下载镜像并 Maven 编译）

### 步骤

```powershell
# 1. 进入项目根目录
cd "e:\code\Spring cloud\warehouse-cursor"

# 2. 创建环境变量文件
copy docker\.env.example .env

# 3. 构建并启动集群（首次约 15~30 分钟）
docker compose up -d --build

# 4. 查看状态
docker compose ps

# 5. 查看日志（等待所有服务 Ready）
docker compose logs -f warehouse-gateway warehouse-web
```

### 访问地址

| 服务 | 地址 |
|------|------|
| **Web 前端** | http://localhost |
| API 网关 | http://localhost:8070/api/goods/page |
| Nacos | http://localhost:8848/nacos |
| Sentinel | http://localhost:8858 |

## 五、课程演示命令

### 1. 验证自定义网络

```powershell
docker network inspect warehouse-net
docker exec warehouse-web ping -c 2 mysql
```

### 2. 验证数据卷持久化

```powershell
# 查看卷
docker volume ls | findstr warehouse

# 在前端新增一条商品后，重启 MySQL 容器
docker compose restart mysql

# 数据应仍然存在
```

### 3. 验证 Web → Gateway → 微服务 → MySQL 链路

```powershell
curl http://localhost/api/goods/page?current=1^&size=10
```

### 4. 查看容器资源

```powershell
docker stats --no-stream
```

## 六、常用运维

```powershell
# 停止集群（保留数据卷）
docker compose down

# 停止并删除数据卷（清空数据库，慎用）
docker compose down -v

# 仅重建 Web 容器
docker compose up -d --build warehouse-web

# 进入 MySQL 容器
docker exec -it warehouse-mysql mysql -uroot -p
```

## 七、故障排查

| 现象 | 处理 |
|------|------|
| 502 / 连接失败 | 等待微服务注册到 Nacos：`docker compose logs warehouse-gateway` |
| MySQL 启动失败 | 检查 `.env` 中密码；删除损坏卷：`docker volume rm warehouse-mysql-data` 后重建 |
| 构建超时 | 配置 Maven 镜像；或本地 `mvn package` 后调整 Dockerfile 直接 COPY jar |
| 80 端口占用 | 修改 `.env` 中 `WEB_PORT=8088` |

## 八、目录结构

```
docker/
├── Dockerfile.service    # Java 微服务通用镜像
├── Dockerfile.web        # Vue + Nginx Web 镜像
├── nginx/nginx.conf      # 反向代理 Gateway
├── mysql/conf.d/         # MySQL 配置（只读挂载）
├── rocketmq/broker.conf  # MQ Broker 配置
├── .env.example          # 环境变量模板
└── README.md             # 本文档
docker-compose.yml        # 集群编排文件（项目根目录）
```
