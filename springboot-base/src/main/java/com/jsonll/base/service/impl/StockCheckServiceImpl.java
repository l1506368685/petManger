package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.Goods;
import com.jsonll.base.entity.InventoryFlow;
import com.jsonll.base.entity.StockCheckItem;
import com.jsonll.base.entity.StockCheckMain;
import com.jsonll.base.mapper.GoodsMapper;
import com.jsonll.base.mapper.InventoryFlowMapper;
import com.jsonll.base.mapper.StockCheckItemMapper;
import com.jsonll.base.mapper.StockCheckMainMapper;
import com.jsonll.base.request.StockCheckPageRequest;
import com.jsonll.base.service.StockCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class StockCheckServiceImpl implements StockCheckService {

    @Autowired
    private StockCheckMainMapper mainMapper;
    @Autowired
    private StockCheckItemMapper itemMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private InventoryFlowMapper inventoryFlowMapper;

    @Override
    public IPage<StockCheckMain> pageList(StockCheckPageRequest request) {
        Page<StockCheckMain> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<StockCheckMain> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getCheckNo())) wrapper.like(StockCheckMain::getCheckNo, request.getCheckNo());
        if (StringUtils.hasText(request.getStatus())) wrapper.eq(StockCheckMain::getStatus, request.getStatus());
        if (request.getStartDate() != null) wrapper.ge(StockCheckMain::getCheckDate, request.getStartDate());
        if (request.getEndDate() != null) wrapper.le(StockCheckMain::getCheckDate, request.getEndDate());
        wrapper.orderByDesc(StockCheckMain::getCreateTime);
        return mainMapper.selectPage(page, wrapper);
    }

    @Override
    public Map<String, Object> detail(Long id) {
        StockCheckMain main = mainMapper.selectById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("main", main);
        List<StockCheckItem> items = itemMapper.selectList(new LambdaQueryWrapper<StockCheckItem>().eq(StockCheckItem::getCheckId, id));
        map.put("items", items);
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(StockCheckMain main) {
        if (main.getCheckNo() == null || main.getCheckNo().isEmpty())
            main.setCheckNo("CK" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4));
        if (main.getCheckDate() == null) main.setCheckDate(LocalDate.now());
        if (main.getStatus() == null) main.setStatus("草稿");
        mainMapper.insert(main);
        List<Goods> goodsList = goodsMapper.selectList(new LambdaQueryWrapper<Goods>().eq(Goods::getStatus, 1));
        for (Goods g : goodsList) {
            StockCheckItem item = new StockCheckItem();
            item.setCheckId(main.getId());
            item.setGoodsId(g.getId());
            item.setGoodsName(g.getGoodsName());
            item.setGoodsCode(g.getGoodsCode());
            item.setBookQuantity(g.getStock() != null ? g.getStock() : 0);
            item.setActualQuantity(item.getBookQuantity());
            item.setDiffQuantity(0);
            itemMapper.insert(item);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(StockCheckMain main, List<StockCheckItem> items) {
        if (main == null || main.getId() == null) throw new RuntimeException("盘点单ID不能为空");
        StockCheckMain existing = mainMapper.selectById(main.getId());
        if (existing == null) throw new RuntimeException("盘点单不存在");
        if ("已确认".equals(existing.getStatus())) throw new RuntimeException("已确认的盘点单不能修改");
        if (main.getCheckDate() != null) existing.setCheckDate(main.getCheckDate());
        if (main.getRemark() != null) existing.setRemark(main.getRemark());
        mainMapper.updateById(existing);
        if (items != null) {
            for (StockCheckItem it : items) {
                if (it.getId() != null && it.getActualQuantity() != null) {
                    it.setDiffQuantity(it.getActualQuantity() - (it.getBookQuantity() != null ? it.getBookQuantity() : 0));
                    itemMapper.updateById(it);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        itemMapper.delete(new LambdaQueryWrapper<StockCheckItem>().eq(StockCheckItem::getCheckId, id));
        mainMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Long id) {
        StockCheckMain main = mainMapper.selectById(id);
        if (main == null) throw new RuntimeException("盘点单不存在");
        if ("已确认".equals(main.getStatus())) throw new RuntimeException("已确认过");
        List<StockCheckItem> items = itemMapper.selectList(new LambdaQueryWrapper<StockCheckItem>().eq(StockCheckItem::getCheckId, id));
        for (StockCheckItem it : items) {
            Integer actual = it.getActualQuantity() != null ? it.getActualQuantity() : it.getBookQuantity();
            if (actual == null) actual = 0;
            Goods g = goodsMapper.selectById(it.getGoodsId());
            if (g == null) continue;
            int book = it.getBookQuantity() != null ? it.getBookQuantity() : 0;
            int diff = actual - book;
            g.setStock(actual);
            goodsMapper.updateById(g);
            if (diff != 0) {
                InventoryFlow flow = new InventoryFlow();
                flow.setFlowNo("IF" + System.currentTimeMillis() + "_" + it.getId());
                flow.setGoodsId(g.getId());
                flow.setGoodsName(g.getGoodsName());
                flow.setFlowType(diff > 0 ? "入库" : "出库");
                flow.setBizType("盘点调整");
                flow.setQuantity(BigDecimal.valueOf(Math.abs(diff)));
                flow.setAfterQuantity(BigDecimal.valueOf(actual));
                flow.setRefNo(main.getCheckNo());
                flow.setFlowTime(LocalDateTime.now());
                flow.setCreateTime(LocalDateTime.now());
                inventoryFlowMapper.insert(flow);
            }
        }
        main.setStatus("已确认");
        main.setConfirmTime(LocalDateTime.now());
        mainMapper.updateById(main);
    }
}
