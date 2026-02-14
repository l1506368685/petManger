package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.Goods;
import com.jsonll.base.entity.InventoryFlow;
import com.jsonll.base.mapper.GoodsMapper;
import com.jsonll.base.mapper.InventoryFlowMapper;
import com.jsonll.base.request.InventoryFlowPageRequest;
import com.jsonll.base.service.InventoryFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryFlowServiceImpl implements InventoryFlowService {

    @Autowired
    private InventoryFlowMapper inventoryFlowMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public IPage<InventoryFlow> pageList(InventoryFlowPageRequest request) {
        Page<InventoryFlow> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<InventoryFlow> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getGoodsName())) wrapper.like(InventoryFlow::getGoodsName, request.getGoodsName());
        if (StringUtils.hasText(request.getFlowType())) wrapper.eq(InventoryFlow::getFlowType, request.getFlowType());
        if (StringUtils.hasText(request.getBizType())) wrapper.eq(InventoryFlow::getBizType, request.getBizType());
        if (request.getStartTime() != null) wrapper.ge(InventoryFlow::getFlowTime, request.getStartTime());
        if (request.getEndTime() != null) wrapper.le(InventoryFlow::getFlowTime, request.getEndTime());
        wrapper.orderByDesc(InventoryFlow::getCreateTime);
        return inventoryFlowMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Map<String, Object>> inventoryList(String goodsName, String goodsCode) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>().eq(Goods::getStatus, 1);
        if (StringUtils.hasText(goodsName)) wrapper.like(Goods::getGoodsName, goodsName);
        if (StringUtils.hasText(goodsCode)) wrapper.like(Goods::getGoodsCode, goodsCode);
        List<Goods> list = goodsMapper.selectList(wrapper);
        return list.stream().map(g -> {
            Map<String, Object> m = new HashMap<>();
            m.put("goodsId", g.getId());
            m.put("goodsName", g.getGoodsName());
            m.put("goodsCode", g.getGoodsCode());
            m.put("stock", g.getStock() != null ? g.getStock() : 0);
            m.put("unit", g.getUnit());
            return m;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> stockAlertList(String goodsName, String alertType) {
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<Goods>().eq(Goods::getStatus, 1);
        if (StringUtils.hasText(goodsName)) wrapper.like(Goods::getGoodsName, goodsName);
        List<Goods> list = goodsMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Goods g : list) {
            int stock = g.getStock() != null ? g.getStock() : 0;
            Integer lower = g.getStockLower();
            Integer upper = g.getStockUpper();
            if (lower != null && stock < lower) {
                if (alertType == null || "采购预警".equals(alertType)) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("goodsId", g.getId());
                    m.put("goodsName", g.getGoodsName());
                    m.put("goodsCode", g.getGoodsCode());
                    m.put("currentStock", stock);
                    m.put("stockLower", lower);
                    m.put("stockUpper", upper);
                    m.put("alertType", "采购预警");
                    m.put("unit", g.getUnit());
                    result.add(m);
                }
            }
            if (upper != null && stock > upper) {
                if (alertType == null || "库存积压预警".equals(alertType)) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("goodsId", g.getId());
                    m.put("goodsName", g.getGoodsName());
                    m.put("goodsCode", g.getGoodsCode());
                    m.put("currentStock", stock);
                    m.put("stockLower", lower);
                    m.put("stockUpper", upper);
                    m.put("alertType", "库存积压预警");
                    m.put("unit", g.getUnit());
                    result.add(m);
                }
            }
        }
        return result;
    }
}
