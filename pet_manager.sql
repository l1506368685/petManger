-- 宠物店管理系统数据库脚本 MySQL 8.0
SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS pet_manager DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pet_manager;

-- 管理员表（登录与管理员管理共用）
DROP TABLE IF EXISTS sys_admin;
CREATE TABLE sys_admin (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE COMMENT '账号',
  password VARCHAR(100) NOT NULL COMMENT '密码',
  `name` VARCHAR(100) COMMENT '姓名',
  `role` VARCHAR(50) COMMENT '角色',
  status INT DEFAULT 1 COMMENT '状态 0禁用 1启用',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '管理员表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
INSERT INTO sys_admin (username, password, `name`, `role`, status) VALUES ('admin', 'admin', 'Admin', 'SuperAdmin', 1);

-- 会员表
DROP TABLE IF EXISTS member;
CREATE TABLE member (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  member_no VARCHAR(50) COMMENT '会员编号',
  name VARCHAR(50) NOT NULL COMMENT '姓名',
  phone VARCHAR(20) NOT NULL COMMENT '手机号',
  `level` VARCHAR(20) COMMENT '会员等级',
  gender VARCHAR(10) COMMENT '性别',
  birthday DATE COMMENT '生日',
  address VARCHAR(200) COMMENT '地址',
  remark VARCHAR(500) COMMENT '备注',
  status INT DEFAULT 1 COMMENT '状态',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '会员表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 会员充值记录表
DROP TABLE IF EXISTS recharge_record;
CREATE TABLE recharge_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(50) NOT NULL COMMENT '订单号',
  member_id BIGINT COMMENT '会员ID',
  member_name VARCHAR(50) COMMENT '会员姓名',
  phone VARCHAR(20) COMMENT '手机号',
  amount DECIMAL(12,2) NOT NULL COMMENT '充值金额',
  pay_amount DECIMAL(12,2) NOT NULL COMMENT '实付金额',
  remark VARCHAR(500) COMMENT '备注',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '会员充值记录表';

-- 宠物类型表
DROP TABLE IF EXISTS pet_type;
CREATE TABLE pet_type (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type_name VARCHAR(50) NOT NULL COMMENT '类型名称',
  sort INT DEFAULT 0 COMMENT '排序',
  status INT DEFAULT 1 COMMENT '状态',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '宠物类型表';
INSERT INTO pet_type (type_name, sort) VALUES ('猫', 1), ('狗', 2);

-- 宠物档案表
DROP TABLE IF EXISTS pet;
CREATE TABLE pet (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  pet_name VARCHAR(50) NOT NULL COMMENT '宠物名称',
  type_id BIGINT COMMENT '宠物类型ID',
  type_name VARCHAR(50) COMMENT '宠物类型名称',
  breed VARCHAR(50) COMMENT '品种',
  color VARCHAR(50) COMMENT '毛色',
  gender VARCHAR(10) COMMENT '性别',
  birthday DATE COMMENT '出生日期',
  contact_name VARCHAR(50) COMMENT '联系人',
  contact_phone VARCHAR(20) COMMENT '联系电话',
  member_id BIGINT COMMENT '会员ID',
  remark VARCHAR(500) COMMENT '备注',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '宠物档案表';

-- 疫苗记录表
DROP TABLE IF EXISTS vaccine_record;
CREATE TABLE vaccine_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  pet_id BIGINT COMMENT '宠物ID',
  pet_name VARCHAR(50) COMMENT '宠物名称',
  vaccine_name VARCHAR(100) COMMENT '疫苗名称',
  batch_no VARCHAR(50) COMMENT '疫苗批号',
  vaccine_date DATE COMMENT '接种日期',
  dose VARCHAR(50) COMMENT '接种剂次',
  part VARCHAR(50) COMMENT '接种部位',
  doctor VARCHAR(50) COMMENT '接种医生',
  fee DECIMAL(10,2) DEFAULT 0 COMMENT '费用',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '疫苗记录表';

-- 医疗记录表
DROP TABLE IF EXISTS medical_record;
CREATE TABLE medical_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  pet_id BIGINT COMMENT '宠物ID',
  pet_name VARCHAR(50) COMMENT '宠物名称',
  visit_date DATE COMMENT '就诊日期',
  symptom VARCHAR(500) COMMENT '主要症状/主诉',
  diagnosis_initial VARCHAR(200) COMMENT '初步诊断',
  diagnosis_final VARCHAR(200) COMMENT '最终诊断',
  doctor VARCHAR(50) COMMENT '主治医生',
  prescription VARCHAR(500) COMMENT '处方/处置说明',
  fee DECIMAL(10,2) DEFAULT 0 COMMENT '费用',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '医疗记录表';

-- 宠物用品表
DROP TABLE IF EXISTS goods;
CREATE TABLE goods (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  goods_name VARCHAR(100) NOT NULL COMMENT '商品名称',
  goods_code VARCHAR(50) COMMENT '商品编码',
  brand VARCHAR(50) COMMENT '品牌',
  price DECIMAL(12,2) NOT NULL COMMENT '售价',
  original_price DECIMAL(12,2) COMMENT '原价',
  stock INT DEFAULT 0 COMMENT '库存',
  intro VARCHAR(500) COMMENT '商品简介',
  unit VARCHAR(20) COMMENT '单位',
  category VARCHAR(50) COMMENT '分类',
  status INT DEFAULT 1 COMMENT '状态',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '宠物用品表';

-- 订单主表
DROP TABLE IF EXISTS order_main;
CREATE TABLE order_main (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(50) NOT NULL COMMENT '订单号',
  member_id BIGINT COMMENT '会员ID',
  member_name VARCHAR(50) COMMENT '会员姓名',
  pet_id BIGINT COMMENT '宠物ID',
  pet_name VARCHAR(50) COMMENT '宠物名称',
  total_amount DECIMAL(12,2) DEFAULT 0 COMMENT '订单总金额',
  status VARCHAR(20) DEFAULT '待付款' COMMENT '订单状态',
  order_time DATETIME COMMENT '下单时间',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '订单主表';

-- 订单明细表
DROP TABLE IF EXISTS order_item;
CREATE TABLE order_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL COMMENT '订单ID',
  goods_id BIGINT COMMENT '商品ID',
  goods_name VARCHAR(100) COMMENT '商品名称',
  quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
  price DECIMAL(12,2) NOT NULL COMMENT '单价',
  amount DECIMAL(12,2) NOT NULL COMMENT '小计金额'
) COMMENT '订单明细表';

-- 索赔记录表
DROP TABLE IF EXISTS claim_record;
CREATE TABLE claim_record (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  claim_no VARCHAR(50) NOT NULL COMMENT '索赔编号',
  pet_id BIGINT COMMENT '宠物ID',
  pet_name VARCHAR(50) COMMENT '宠物名称',
  event_date DATE COMMENT '事件日期',
  claim_amount DECIMAL(12,2) COMMENT '索赔金额',
  final_amount DECIMAL(12,2) COMMENT '最终索赔金额',
  pay_method VARCHAR(50) COMMENT '赔付方式',
  pay_time DATETIME COMMENT '赔付时间',
  reason VARCHAR(500) COMMENT '索赔原因',
  remark VARCHAR(500) COMMENT '备注',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '索赔记录表';

-- 采购记录表
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

-- 库存流水表
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

-- 数据字典类型表
DROP TABLE IF EXISTS sys_dict_type;
CREATE TABLE sys_dict_type (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  dict_type VARCHAR(50) NOT NULL UNIQUE COMMENT '类型编码',
  dict_name VARCHAR(100) NOT NULL COMMENT '类型名称',
  remark VARCHAR(200) COMMENT '备注',
  sort INT DEFAULT 0 COMMENT '排序',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '数据字典类型表' DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 数据字典项表
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
