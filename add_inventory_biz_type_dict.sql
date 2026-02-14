-- 库存流水业务类型：表字段 + 数据字典
SET NAMES utf8mb4;
USE pet_manager;

-- 库存流水表增加业务类型字段
ALTER TABLE inventory_flow ADD COLUMN biz_type VARCHAR(32) DEFAULT NULL COMMENT '业务类型：正常入库、正常出库、退库' AFTER flow_type;

-- 数据字典：库存业务类型
INSERT IGNORE INTO sys_dict_type (dict_type, dict_name, sort) VALUES ('inventory_biz_type', '库存业务类型', 5);
INSERT INTO sys_dict_item (dict_type, item_label, item_value, sort) SELECT 'inventory_biz_type', '正常入库', '正常入库', 1 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_type = 'inventory_biz_type' AND item_value = '正常入库');
INSERT INTO sys_dict_item (dict_type, item_label, item_value, sort) SELECT 'inventory_biz_type', '正常出库', '正常出库', 2 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_type = 'inventory_biz_type' AND item_value = '正常出库');
INSERT INTO sys_dict_item (dict_type, item_label, item_value, sort) SELECT 'inventory_biz_type', '退库', '退库', 3 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_type = 'inventory_biz_type' AND item_value = '退库');
