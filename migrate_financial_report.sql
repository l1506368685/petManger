-- 多维度财务报表：支付方式、成本字段、支付方式字典
SET NAMES utf8mb4;
USE pet_manager;

-- 订单表增加支付方式（日报按现金/微信/支付宝分类）
ALTER TABLE order_main ADD COLUMN pay_method VARCHAR(50) DEFAULT '现金' COMMENT '支付方式：现金/微信/支付宝/转账' AFTER status;

-- 充值记录表增加支付方式
ALTER TABLE recharge_record ADD COLUMN pay_method VARCHAR(50) DEFAULT '现金' COMMENT '支付方式' AFTER pay_amount;

-- 商品表增加成本价（用于毛利、毛利率计算）
ALTER TABLE goods ADD COLUMN cost_price DECIMAL(12,2) DEFAULT 0 COMMENT '成本价' AFTER price;

-- 订单明细表增加单位成本（下单时从商品快照，用于历史毛利分析）
ALTER TABLE order_item ADD COLUMN cost DECIMAL(12,2) DEFAULT 0 COMMENT '单位成本' AFTER price;

-- 支付方式字典（与赔付方式 pay_method 区分，用于收银支付方式）
INSERT IGNORE INTO sys_dict_type (dict_type, dict_name, sort) VALUES ('payment_method', '支付方式', 10);
INSERT IGNORE INTO sys_dict_item (dict_type, item_label, item_value, sort) VALUES ('payment_method', '现金', '现金', 1);
INSERT IGNORE INTO sys_dict_item (dict_type, item_label, item_value, sort) VALUES ('payment_method', '微信', '微信', 2);
INSERT IGNORE INTO sys_dict_item (dict_type, item_label, item_value, sort) VALUES ('payment_method', '支付宝', '支付宝', 3);
INSERT IGNORE INTO sys_dict_item (dict_type, item_label, item_value, sort) VALUES ('payment_method', '转账', '转账', 4);
