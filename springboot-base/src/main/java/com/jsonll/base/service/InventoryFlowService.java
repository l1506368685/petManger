package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.InventoryFlow;
import com.jsonll.base.request.InventoryFlowPageRequest;

import java.util.List;
import java.util.Map;

public interface InventoryFlowService {
    IPage<InventoryFlow> pageList(InventoryFlowPageRequest request);
    List<Map<String, Object>> inventoryList(String goodsName, String goodsCode);
    /** 库存预警列表：采购预警=库存低于下限，库存积压预警=库存高于上限。alertType 为空查全部。 */
    List<Map<String, Object>> stockAlertList(String goodsName, String alertType);
}
