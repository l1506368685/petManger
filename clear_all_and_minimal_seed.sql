-- Step1: encoding + clear all business data
SET NAMES utf8mb4;
USE pet_manager;

ALTER DATABASE pet_manager CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

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

-- Step2: minimal test data (Chinese only for verification)
INSERT INTO member (member_no, name, phone, balance, `level`, gender, birthday, address, remark, status, deleted, create_time, update_time) VALUES
('M001', '张三', '13800000001', 100.00, '普通会员', '男', '1990-01-01', '北京市朝阳区', NULL, 1, 0, NOW(), NOW()),
('M002', '李四', '13800000002', 200.00, '白银会员', '女', '1992-05-15', '北京市海淀区', NULL, 1, 0, NOW(), NOW());

INSERT INTO goods (goods_name, goods_code, brand, price, original_price, stock, intro, unit, category, status, deleted, create_time, update_time) VALUES
('皇家成猫粮', 'G001', '皇家', 128.00, 158.00, 50, '法国皇家成猫配方', '袋', '猫粮', 1, 0, NOW(), NOW()),
('冠能幼犬粮', 'G002', '冠能', 98.00, 118.00, 40, '幼犬成长配方', '袋', '狗粮', 1, 0, NOW(), NOW());

INSERT INTO pet (pet_name, type_id, type_name, breed, color, gender, birthday, contact_name, contact_phone, member_id, remark, deleted, create_time, update_time) VALUES
('咪咪', 1, '猫', '英国短毛猫', '蓝灰', '母', '2023-06-01', '张三', '13800000001', 1, NULL, 0, NOW(), NOW()),
('旺财', 2, '狗', '金毛', '金色', '公', '2022-03-01', '李四', '13800000002', 2, NULL, 0, NOW(), NOW());

INSERT INTO recharge_record (order_no, member_id, member_name, phone, amount, pay_amount, remark, create_time) VALUES
('R001', 1, '张三', '13800000001', 100.00, 100.00, '测试充值', NOW());

INSERT INTO order_main (order_no, member_id, member_name, pet_id, pet_name, total_amount, status, order_time, deleted, create_time, update_time) VALUES
('O001', 1, '张三', 1, '咪咪', 128.00, '已完成', NOW(), 0, NOW(), NOW());

INSERT INTO order_item (order_id, goods_id, goods_name, quantity, price, amount) VALUES
(1, 1, '皇家成猫粮', 1, 128.00, 128.00);

UPDATE goods SET stock = stock - 1 WHERE id = 1;
