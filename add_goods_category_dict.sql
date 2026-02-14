-- 新增「商品分类」字典类型及示例项（在数据字典中维护产品分类后，宠物用品页从该字典选择）
SET NAMES utf8mb4;
USE pet_manager;

INSERT IGNORE INTO sys_dict_type (dict_type, dict_name, sort) VALUES ('goods_category', '商品分类', 4);
INSERT INTO sys_dict_item (dict_type, item_label, item_value, sort) SELECT 'goods_category', '猫粮', '猫粮', 1 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_type = 'goods_category' AND item_value = '猫粮');
INSERT INTO sys_dict_item (dict_type, item_label, item_value, sort) SELECT 'goods_category', '狗粮', '狗粮', 2 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_type = 'goods_category' AND item_value = '狗粮');
INSERT INTO sys_dict_item (dict_type, item_label, item_value, sort) SELECT 'goods_category', '零食', '零食', 3 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_type = 'goods_category' AND item_value = '零食');
INSERT INTO sys_dict_item (dict_type, item_label, item_value, sort) SELECT 'goods_category', '用品', '用品', 4 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_item WHERE dict_type = 'goods_category' AND item_value = '用品');
