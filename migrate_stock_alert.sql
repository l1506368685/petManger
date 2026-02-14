-- 7.5 商品库存预警 + 保质期 + 盘点
SET NAMES utf8mb4;
USE pet_manager;

-- 商品表：库存上下限（若已执行过可跳过）
ALTER TABLE goods ADD COLUMN stock_lower INT DEFAULT NULL COMMENT '库存下限，低于时采购预警' AFTER stock;
ALTER TABLE goods ADD COLUMN stock_upper INT DEFAULT NULL COMMENT '库存上限，高于时库存积压预警' AFTER stock_lower;

-- 采购记录表：批次与保质期
ALTER TABLE purchase_record ADD COLUMN batch_no VARCHAR(50) DEFAULT NULL COMMENT '批次号' AFTER purchase_date;
ALTER TABLE purchase_record ADD COLUMN production_date DATE DEFAULT NULL COMMENT '生产日期' AFTER batch_no;
ALTER TABLE purchase_record ADD COLUMN expiry_date DATE DEFAULT NULL COMMENT '到期日，用于保质期预警' AFTER production_date;

-- 盘点单主表
CREATE TABLE IF NOT EXISTS stock_check_main (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  check_no VARCHAR(50) NOT NULL COMMENT '盘点单号',
  check_date DATE COMMENT '盘点日期',
  status VARCHAR(20) DEFAULT '草稿' COMMENT '状态：草稿/已确认',
  remark VARCHAR(500) COMMENT '备注',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  confirm_time DATETIME COMMENT '确认时间'
) COMMENT '盘点单主表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 盘点明细表
CREATE TABLE IF NOT EXISTS stock_check_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  check_id BIGINT NOT NULL COMMENT '盘点单ID',
  goods_id BIGINT NOT NULL COMMENT '商品ID',
  goods_name VARCHAR(100) COMMENT '商品名称',
  goods_code VARCHAR(50) COMMENT '商品编码',
  book_quantity INT DEFAULT 0 COMMENT '账面数量',
  actual_quantity INT DEFAULT NULL COMMENT '实盘数量',
  diff_quantity INT DEFAULT NULL COMMENT '盈亏数量=实盘-账面',
  remark VARCHAR(200) COMMENT '备注',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '盘点明细表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
