package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.Goods;
import com.jsonll.base.request.InventoryFlowPageRequest;
import com.jsonll.base.service.GoodsService;
import com.jsonll.base.service.InventoryFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryFlowService inventoryFlowService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list")
    public R<List<Map<String, Object>>> list(@RequestParam(required = false) String goodsName, @RequestParam(required = false) String goodsCode) {
        return R.ok(inventoryFlowService.inventoryList(goodsName, goodsCode));
    }

    @PutMapping("/updateStock")
    public R<Void> updateStock(@RequestBody Goods goods) {
        if (goods.getId() == null) throw new RuntimeException("商品ID不能为空");
        goodsService.updateStock(goods.getId(), goods.getStock());
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        goodsService.delete(id);
        return R.ok();
    }

    @GetMapping("/flow/pageList")
    public R<?> flowPageList(InventoryFlowPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(inventoryFlowService.pageList(request));
    }

    /** 库存预警列表：采购预警、库存积压预警 */
    @GetMapping("/alertList")
    public R<List<Map<String, Object>>> alertList(@RequestParam(required = false) String goodsName, @RequestParam(required = false) String alertType) {
        return R.ok(inventoryFlowService.stockAlertList(goodsName, alertType));
    }
}
