-- 若已初始化过库但缺少 stock 表，单独执行本脚本即可
USE warehouse_stock;

CREATE TABLE IF NOT EXISTS stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    goods_id BIGINT NOT NULL,
    stock_num INT DEFAULT 0,
    UNIQUE KEY uk_goods_id (goods_id)
);

INSERT INTO stock(goods_id, stock_num) VALUES(1, 100)
ON DUPLICATE KEY UPDATE stock_num = VALUES(stock_num);
