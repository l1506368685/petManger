package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.Goods;
import com.jsonll.base.entity.InventoryFlow;
import com.jsonll.base.entity.PurchaseRecord;
import com.jsonll.base.mapper.GoodsMapper;
import com.jsonll.base.mapper.InventoryFlowMapper;
import com.jsonll.base.mapper.PurchaseRecordMapper;
import com.jsonll.base.request.PurchasePageRequest;
import com.jsonll.base.service.PurchaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseRecordServiceImpl implements PurchaseRecordService {

    @Autowired
    private PurchaseRecordMapper purchaseRecordMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private InventoryFlowMapper inventoryFlowMapper;

    @Override
    public IPage<PurchaseRecord> pageList(PurchasePageRequest request) {
        Page<PurchaseRecord> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<PurchaseRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getPurchaseNo())) wrapper.like(PurchaseRecord::getPurchaseNo, request.getPurchaseNo());
        if (StringUtils.hasText(request.getGoodsName())) wrapper.like(PurchaseRecord::getGoodsName, request.getGoodsName());
        if (StringUtils.hasText(request.getSupplier())) wrapper.like(PurchaseRecord::getSupplier, request.getSupplier());
        if (request.getStartDate() != null) wrapper.ge(PurchaseRecord::getPurchaseDate, request.getStartDate());
        if (request.getEndDate() != null) wrapper.le(PurchaseRecord::getPurchaseDate, request.getEndDate());
        wrapper.orderByDesc(PurchaseRecord::getPurchaseDate);
        return purchaseRecordMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(PurchaseRecord record) {
        if (record.getPurchaseNo() == null || record.getPurchaseNo().isEmpty()) {
            record.setPurchaseNo("P" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4));
        }
        if (record.getPurchaseDate() == null) record.setPurchaseDate(LocalDate.now());
        if (record.getQuantity() != null && record.getPrice() != null) {
            record.setAmount(record.getQuantity().multiply(record.getPrice()));
        }
        purchaseRecordMapper.insert(record);
        // 入库：增加库存并写流水
        if (record.getQuantity() == null || record.getQuantity().compareTo(BigDecimal.ZERO) <= 0) return;
        Long goodsId = record.getGoodsId();
        if (goodsId == null && StringUtils.hasText(record.getGoodsName())) {
            List<Goods> list = goodsMapper.selectList(new LambdaQueryWrapper<Goods>().eq(Goods::getGoodsName, record.getGoodsName()).last("limit 1"));
            if (!list.isEmpty()) goodsId = list.get(0).getId();
        }
        if (goodsId == null) return;
        Goods g = goodsMapper.selectById(goodsId);
        if (g == null) return;
        int addQty = record.getQuantity().intValue();
        if (addQty <= 0) return;
        int oldStock = g.getStock() == null ? 0 : g.getStock();
        int newStock = oldStock + addQty;
        g.setStock(newStock);
        goodsMapper.updateById(g);
        InventoryFlow flow = new InventoryFlow();
        flow.setFlowNo("IF" + System.currentTimeMillis());
        flow.setGoodsId(g.getId());
        flow.setGoodsName(g.getGoodsName());
        flow.setFlowType("入库");
        flow.setBizType("正常入库");
        flow.setQuantity(BigDecimal.valueOf(addQty));
        flow.setAfterQuantity(BigDecimal.valueOf(newStock));
        flow.setRefNo(record.getPurchaseNo());
        flow.setFlowTime(LocalDateTime.now());
        flow.setCreateTime(LocalDateTime.now());
        inventoryFlowMapper.insert(flow);
    }

    @Override
    public void update(PurchaseRecord record) {
        if (record.getQuantity() != null && record.getPrice() != null) {
            record.setAmount(record.getQuantity().multiply(record.getPrice()));
        }
        purchaseRecordMapper.updateById(record);
    }

    @Override
    public void delete(Long id) {
        purchaseRecordMapper.deleteById(id);
    }

    @Override
    public PurchaseRecord detail(Long id) {
        return purchaseRecordMapper.selectById(id);
    }

    @Override
    public List<PurchaseRecord> expiryAlertList(Integer days) {
        int d = (days != null) ? days : 30;
        LocalDate end = LocalDate.now().plusDays(d);
        LambdaQueryWrapper<PurchaseRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(PurchaseRecord::getExpiryDate);
        wrapper.le(PurchaseRecord::getExpiryDate, end);
        wrapper.orderByAsc(PurchaseRecord::getExpiryDate);
        return purchaseRecordMapper.selectList(wrapper);
    }
}
