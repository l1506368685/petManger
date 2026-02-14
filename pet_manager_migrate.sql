-- 增量脚本：采购、库存流水、数据字典表（若已有 claim_record 且无 update_time，请先执行：ALTER TABLE claim_record ADD COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;）
SET NAMES utf8mb4;
USE pet_manager;

DROP TABLE IF EXISTS purchase_record;
CREATE TABLE purchase_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  purchase_no VARCHAR(50) NOT NULL COMMENT '采购单号',
  goods_id BIGINT COMMENT '商品ID',
  goods_name VARCHAR(100) COMMENT '商品名称',
  spec VARCHAR(100) COMMENT '规格',
  quantity DECIMAL(12,2) NOT NULL DEFAULT 1 COMMENT '采购数量',
  price DECIMAL(12,2) NOT NULL COMMENT '单价',
  amount DECIMAL(12,2) COMMENT '金额',
  supplier VARCHAR(100) COMMENT '供应商',
  purchase_date DATE COMMENT '采购日期',
  remark VARCHAR(500) COMMENT '备注',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '采购记录表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS inventory_flow;
CREATE TABLE inventory_flow (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  flow_no VARCHAR(50) COMMENT '流水号',
  goods_id BIGINT COMMENT '商品ID',
  goods_name VARCHAR(100) COMMENT '商品名称',
  flow_type VARCHAR(20) NOT NULL COMMENT '类型：IN-入库 OUT-出库',
  quantity DECIMAL(12,2) NOT NULL COMMENT '数量',
  after_quantity DECIMAL(12,2) COMMENT '变更后库存',
  ref_no VARCHAR(50) COMMENT '关联单号',
  flow_time DATETIME COMMENT '业务时间',
  remark VARCHAR(500) COMMENT '备注',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '库存流水表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS sys_dict_type;
CREATE TABLE sys_dict_type (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  dict_type VARCHAR(50) NOT NULL UNIQUE COMMENT '类型编码',
  dict_name VARCHAR(100) NOT NULL COMMENT '类型名称',
  remark VARCHAR(200) COMMENT '备注',
  sort INT DEFAULT 0 COMMENT '排序',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '数据字典类型表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS sys_dict_item;
CREATE TABLE sys_dict_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  dict_type VARCHAR(50) NOT NULL COMMENT '类型编码',
  item_label VARCHAR(100) NOT NULL COMMENT '显示标签',
  item_value VARCHAR(100) NOT NULL COMMENT '选项值',
  sort INT DEFAULT 0 COMMENT '排序',
  status INT DEFAULT 1 COMMENT '状态 0禁用 1启用',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '数据字典项表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO sys_dict_type (dict_type, dict_name, sort) VALUES ('pay_method', '赔付方式', 1), ('order_status', '订单状态', 2), ('member_level', '会员等级', 3);
INSERT INTO sys_dict_item (dict_type, item_label, item_value, sort) VALUES ('pay_method', '现金', '现金', 1), ('pay_method', '转账', '转账', 2), ('order_status', '待付款', '待付款', 1), ('order_status', '已付款', '已付款', 2), ('order_status', '已发货', '已发货', 3), ('order_status', '已完成', '已完成', 4), ('member_level', '普通会员', '普通会员', 1), ('member_level', '银卡会员', '银卡会员', 2), ('member_level', '金卡会员', '金卡会员', 3);
