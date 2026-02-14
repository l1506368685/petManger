-- 7.3 会员储值卡/套餐卡
-- 执行前请备份数据库。卡类型、会员持卡、交易流水。

SET NAMES utf8mb4;
USE pet_manager;

-- 卡类型表：储值卡/次卡、面值、售价、有效期、赠送规则
DROP TABLE IF EXISTS card_type;
CREATE TABLE card_type (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type_name VARCHAR(100) NOT NULL COMMENT '卡类型名称',
  card_kind TINYINT NOT NULL DEFAULT 1 COMMENT '卡种 1储值卡 2次卡',
  face_value DECIMAL(12,2) DEFAULT 0 COMMENT '面值(储值卡)或单次价值(次卡)',
  price DECIMAL(12,2) NOT NULL COMMENT '售价',
  valid_days INT DEFAULT 365 COMMENT '有效天数',
  total_times INT DEFAULT 0 COMMENT '次卡总次数(次卡有效)',
  gift_rule VARCHAR(200) COMMENT '赠送规则 如充500送50',
  max_sub_cards INT DEFAULT 0 COMMENT '可绑定副卡数 0不可绑定',
  sort INT DEFAULT 0,
  status INT DEFAULT 1 COMMENT '0禁用 1启用',
  deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '卡类型表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 会员卡表：会员持有的卡
DROP TABLE IF EXISTS member_card;
CREATE TABLE member_card (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  card_no VARCHAR(50) NOT NULL COMMENT '卡号',
  member_id BIGINT NOT NULL COMMENT '会员ID',
  member_name VARCHAR(50) COMMENT '会员姓名',
  card_type_id BIGINT NOT NULL COMMENT '卡类型ID',
  card_type_name VARCHAR(100) COMMENT '卡类型名称',
  card_kind TINYINT NOT NULL DEFAULT 1 COMMENT '1储值卡 2次卡',
  balance DECIMAL(12,2) DEFAULT 0 COMMENT '储值余额',
  total_times INT DEFAULT 0 COMMENT '次卡总次数',
  remain_times INT DEFAULT 0 COMMENT '剩余次数',
  expire_time DATETIME COMMENT '到期时间',
  main_card_id BIGINT DEFAULT NULL COMMENT '主卡ID(副卡时)',
  status INT DEFAULT 1 COMMENT '0禁用 1正常 2已过期',
  deleted TINYINT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '会员卡表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 卡交易流水表
DROP TABLE IF EXISTS card_transaction;
CREATE TABLE card_transaction (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  card_id BIGINT NOT NULL COMMENT '会员卡ID',
  member_id BIGINT COMMENT '会员ID',
  member_name VARCHAR(50) COMMENT '会员姓名',
  trans_type VARCHAR(20) NOT NULL COMMENT 'recharge购卡充值 consume消费 gift赠送 expire过期',
  amount DECIMAL(12,2) DEFAULT 0 COMMENT '金额变动',
  times_before INT DEFAULT 0 COMMENT '变动前次数',
  times_change INT DEFAULT 0 COMMENT '次数变动',
  times_after INT DEFAULT 0 COMMENT '变动后次数',
  balance_before DECIMAL(12,2) DEFAULT 0,
  balance_after DECIMAL(12,2) DEFAULT 0,
  remark VARCHAR(500) COMMENT '备注',
  biz_order_no VARCHAR(50) COMMENT '关联业务单号',
  deleted TINYINT DEFAULT 0,
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '卡交易流水表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
