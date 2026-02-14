-- 7.1 会员生命周期管理 - 数据库迁移
-- 执行前请备份数据库。MySQL 8.0。

USE pet_manager;

-- 1. 会员表扩展：最后消费时间、标签(JSON)
ALTER TABLE member ADD COLUMN last_consume_time DATETIME NULL COMMENT '最后消费时间' AFTER balance;
ALTER TABLE member ADD COLUMN tags VARCHAR(500) NULL COMMENT '标签JSON数组，如 ["沉睡","高价值"]' AFTER last_consume_time;

-- 2. 会员等级规则表：消费金额区间对应等级
DROP TABLE IF EXISTS member_level_rule;
CREATE TABLE member_level_rule (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  level_name VARCHAR(50) NOT NULL COMMENT '等级名称，如普通会员、白银会员',
  min_amount DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '近12个月消费金额下限(含)',
  max_amount DECIMAL(12,2) NOT NULL COMMENT '近12个月消费金额上限(含)，999999表示无上限',
  sort_order INT DEFAULT 0 COMMENT '排序，升序',
  status INT DEFAULT 1 COMMENT '状态 0禁用 1启用',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '会员等级规则表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO member_level_rule (level_name, min_amount, max_amount, sort_order) VALUES
('普通会员', 0, 1000, 1),
('白银会员', 1000, 3000, 2),
('黄金会员', 3000, 10000, 3),
('钻石会员', 10000, 999999, 4);

-- 3. 会员生命周期配置表：流失预警天数、沉睡注册天数等
DROP TABLE IF EXISTS member_lifecycle_config;
CREATE TABLE member_lifecycle_config (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  config_key VARCHAR(50) NOT NULL UNIQUE COMMENT '配置键',
  config_value VARCHAR(200) NOT NULL COMMENT '配置值',
  remark VARCHAR(200) COMMENT '说明',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '会员生命周期配置' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO member_lifecycle_config (config_key, config_value, remark) VALUES
('churn_warning_days', '90', '流失预警：最后消费超过此天数视为流失预警'),
('sleeping_register_days', '30', '沉睡会员：注册超过此天数且从未消费视为沉睡');

-- 4. 流失/沉睡预警记录表：便于列表展示与记录
DROP TABLE IF EXISTS member_lifecycle_warning;
CREATE TABLE member_lifecycle_warning (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  member_id BIGINT NOT NULL COMMENT '会员ID',
  member_name VARCHAR(50) COMMENT '会员姓名',
  phone VARCHAR(20) COMMENT '手机号',
  warning_type VARCHAR(20) NOT NULL COMMENT '预警类型：CHURN流失预警 / SLEEPING沉睡',
  last_consume_time DATETIME COMMENT '最后消费时间(流失时有值)',
  register_days INT COMMENT '注册天数(沉睡时有值)',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '扫描生成时间'
) COMMENT '会员生命周期预警记录' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_member_lifecycle_warning_type_time ON member_lifecycle_warning(warning_type, create_time);
