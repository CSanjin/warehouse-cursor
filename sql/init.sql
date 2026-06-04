-- 仓储微服务数据库初始化脚本
-- MySQL 8.x  root/root

CREATE DATABASE IF NOT EXISTS warehouse_user DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS warehouse_goods DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS warehouse_base DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS warehouse_stock DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS warehouse_inout DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS warehouse_check DEFAULT CHARACTER SET utf8mb4;

USE warehouse_user;

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    password VARCHAR(128) NOT NULL,
    real_name VARCHAR(64),
    phone VARCHAR(20),
    email VARCHAR(128),
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    create_by VARCHAR(64),
    update_by VARCHAR(64),
    deleted INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_code VARCHAR(64) NOT NULL,
    role_name VARCHAR(64) NOT NULL,
    remark VARCHAR(255),
    create_time DATETIME,
    update_time DATETIME,
    create_by VARCHAR(64),
    update_by VARCHAR(64),
    deleted INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    perm_code VARCHAR(64) NOT NULL,
    perm_name VARCHAR(64) NOT NULL,
    url VARCHAR(255),
    method VARCHAR(16),
    parent_id BIGINT DEFAULT 0,
    create_time DATETIME,
    update_time DATETIME,
    create_by VARCHAR(64),
    update_by VARCHAR(64),
    deleted INT DEFAULT 0
);

INSERT INTO sys_user(username,password,real_name,status) VALUES('admin','123456','管理员',1);
INSERT INTO sys_role(role_code,role_name) VALUES('ADMIN','系统管理员');
INSERT INTO sys_permission(perm_code,perm_name,url,method) VALUES('user:list','用户查询','/api/user/**','GET');

USE warehouse_goods;

CREATE TABLE IF NOT EXISTS goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_code VARCHAR(64) NOT NULL,
    goods_name VARCHAR(128) NOT NULL,
    category VARCHAR(64),
    unit VARCHAR(16),
    price DECIMAL(12,2),
    spec VARCHAR(128),
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    create_by VARCHAR(64),
    update_by VARCHAR(64),
    deleted INT DEFAULT 0
);

INSERT INTO goods(goods_code,goods_name,category,unit,price,status) VALUES('G001','螺丝M8','五金','个',0.50,1);

USE warehouse_base;

CREATE TABLE IF NOT EXISTS warehouse_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_code VARCHAR(64) NOT NULL,
    warehouse_name VARCHAR(128) NOT NULL,
    address VARCHAR(255),
    manager VARCHAR(64),
    phone VARCHAR(20),
    status INT DEFAULT 1,
    create_time DATETIME,
    update_time DATETIME,
    create_by VARCHAR(64),
    update_by VARCHAR(64),
    deleted INT DEFAULT 0
);

INSERT INTO warehouse_info(warehouse_code,warehouse_name,address,status) VALUES('WH001','主仓库','上海市浦东新区',1);

USE warehouse_stock;

-- Seata 分布式事务 / StockService 使用的库存表（按商品维度）
CREATE TABLE IF NOT EXISTS stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL,
    stock_num INT DEFAULT 0,
    UNIQUE KEY uk_goods_id (goods_id)
);

INSERT INTO stock(goods_id, stock_num) VALUES(1, 100)
ON DUPLICATE KEY UPDATE stock_num = stock_num;

CREATE TABLE IF NOT EXISTS stock_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    warehouse_id BIGINT NOT NULL,
    goods_id BIGINT NOT NULL,
    quantity INT DEFAULT 0,
    lock_quantity INT DEFAULT 0,
    remark VARCHAR(255),
    create_time DATETIME,
    update_time DATETIME,
    create_by VARCHAR(64),
    update_by VARCHAR(64),
    deleted INT DEFAULT 0,
    UNIQUE KEY uk_wh_goods (warehouse_id, goods_id)
);

USE warehouse_stock;

CREATE TABLE IF NOT EXISTS undo_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    branch_id BIGINT NOT NULL,
    xid VARCHAR(128) NOT NULL,
    context VARCHAR(128) NOT NULL,
    rollback_info LONGBLOB NOT NULL,
    log_status INT NOT NULL,
    log_created DATETIME NOT NULL,
    log_modified DATETIME NOT NULL,
    ext VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

USE warehouse_inout;

CREATE TABLE IF NOT EXISTS undo_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    branch_id BIGINT NOT NULL,
    xid VARCHAR(128) NOT NULL,
    context VARCHAR(128) NOT NULL,
    rollback_info LONGBLOB NOT NULL,
    log_status INT NOT NULL,
    log_created DATETIME NOT NULL,
    log_modified DATETIME NOT NULL,
    ext VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inout_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL,
    inout_num INT NOT NULL,
    order_no VARCHAR(64),
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS stock_inout (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL,
    order_type VARCHAR(16) NOT NULL,
    warehouse_id BIGINT NOT NULL,
    goods_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    operator VARCHAR(64),
    status INT DEFAULT 0,
    remark VARCHAR(255),
    create_time DATETIME,
    update_time DATETIME,
    create_by VARCHAR(64),
    update_by VARCHAR(64),
    deleted INT DEFAULT 0
);

USE warehouse_check;

CREATE TABLE IF NOT EXISTS stock_check (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    check_no VARCHAR(64) NOT NULL,
    warehouse_id BIGINT NOT NULL,
    goods_id BIGINT NOT NULL,
    book_quantity INT,
    actual_quantity INT NOT NULL,
    diff_quantity INT,
    operator VARCHAR(64),
    status INT DEFAULT 0,
    remark VARCHAR(255),
    create_time DATETIME,
    update_time DATETIME,
    create_by VARCHAR(64),
    update_by VARCHAR(64),
    deleted INT DEFAULT 0
);
