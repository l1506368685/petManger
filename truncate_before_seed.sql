-- ============================================================
-- 清空业务表历史数据（用于重新导入测试数据前执行）
-- 不删：sys_admin、pet_type、sys_dict_type、sys_dict_item
-- ============================================================
SET NAMES utf8mb4;
USE pet_manager;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE inventory_flow;
TRUNCATE TABLE order_item;
TRUNCATE TABLE order_main;
TRUNCATE TABLE purchase_record;
TRUNCATE TABLE vaccine_record;
TRUNCATE TABLE medical_record;
TRUNCATE TABLE claim_record;
TRUNCATE TABLE recharge_record;
TRUNCATE TABLE pet;
TRUNCATE TABLE goods;
TRUNCATE TABLE member;

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'OK: 业务表已清空，可执行 seed_test_data_one_month.sql 重新导入' AS msg;
