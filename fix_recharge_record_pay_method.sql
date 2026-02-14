-- 为 recharge_record 表添加 pay_method 列（若表来自初始建表脚本且未执行过 migrate_financial_report.sql）
-- 在 MySQL 中执行： source 本文件路径  或在客户端执行下面语句

USE pet_manager;

ALTER TABLE recharge_record ADD COLUMN pay_method VARCHAR(50) DEFAULT '现金' COMMENT '支付方式' AFTER pay_amount;
