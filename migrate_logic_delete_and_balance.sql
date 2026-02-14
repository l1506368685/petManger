-- 逻辑删除字段 + 会员余额
-- 执行前请备份数据库

-- 会员表：当前余额
ALTER TABLE member ADD COLUMN balance DECIMAL(12,2) DEFAULT 0 COMMENT '当前余额';

-- 各业务表：逻辑删除字段 deleted，0=正常 1=已删除
ALTER TABLE member ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE goods ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE pet_type ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE pet ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE order_main ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE order_item ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE purchase_record ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE recharge_record ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE claim_record ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE vaccine_record ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE medical_record ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE inventory_flow ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE sys_dict_type ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE sys_dict_item ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';
ALTER TABLE sys_admin ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0否1是';

-- 若表已存在该列可忽略报错，或先检查再执行：
-- SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='member' AND COLUMN_NAME='balance';
