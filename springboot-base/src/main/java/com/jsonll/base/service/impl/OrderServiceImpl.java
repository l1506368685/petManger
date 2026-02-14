package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.Goods;
import com.jsonll.base.entity.InventoryFlow;
import com.jsonll.base.entity.OrderItem;
import com.jsonll.base.entity.OrderMain;
import com.jsonll.base.mapper.GoodsMapper;
import com.jsonll.base.mapper.InventoryFlowMapper;
import com.jsonll.base.mapper.OrderItemMapper;
import com.jsonll.base.mapper.OrderMainMapper;
import com.jsonll.base.request.OrderPageRequest;
import com.jsonll.base.request.OrderSaveDTO;
import com.jsonll.base.service.MemberLifecycleService;
import com.jsonll.base.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMainMapper orderMainMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private InventoryFlowMapper inventoryFlowMapper;
    @Autowired
    private MemberLifecycleService memberLifecycleService;

    @Override
    public IPage<OrderMain> pageList(OrderPageRequest request) {
        Page<OrderMain> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<OrderMain> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getOrderNo())) wrapper.like(OrderMain::getOrderNo, request.getOrderNo());
        if (StringUtils.hasText(request.getPetName())) wrapper.like(OrderMain::getPetName, request.getPetName());
        if (StringUtils.hasText(request.getMemberName())) wrapper.like(OrderMain::getMemberName, request.getMemberName());
        if (StringUtils.hasText(request.getStatus())) wrapper.eq(OrderMain::getStatus, request.getStatus());
        if (request.getStartTime() != null) wrapper.ge(OrderMain::getOrderTime, request.getStartTime());
        if (request.getEndTime() != null) wrapper.le(OrderMain::getOrderTime, request.getEndTime());
        wrapper.orderByDesc(OrderMain::getOrderTime);
        IPage<OrderMain> result = orderMainMapper.selectPage(page, wrapper);
        List<OrderMain> records = result.getRecords();
        if (!records.isEmpty()) {
            List<Long> orderIds = records.stream().map(OrderMain::getId).collect(Collectors.toList());
            List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().in(OrderItem::getOrderId, orderIds));
            Map<Long, Integer> qtyMap = new HashMap<>();
            for (OrderItem it : items) {
                Long oid = it.getOrderId();
                int q = it.getQuantity() != null ? it.getQuantity() : 0;
                qtyMap.merge(oid, q, Integer::sum);
            }
            for (OrderMain o : records) o.setTotalQuantity(qtyMap.getOrDefault(o.getId(), 0));
        }
        return result;
    }

    @Override
    public Map<String, Object> detail(Long id) {
        OrderMain main = orderMainMapper.selectById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("order", main);
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, id);
        map.put("items", orderItemMapper.selectList(wrapper));
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(OrderSaveDTO dto) {
        OrderMain main = dto.getOrder();
        if (main == null) throw new RuntimeException("订单主表不能为空");
        if (main.getOrderNo() == null || main.getOrderNo().isEmpty())
            main.setOrderNo("O" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4));
        if (main.getOrderTime() == null) main.setOrderTime(LocalDateTime.now());
        if (main.getStatus() == null) main.setStatus("待付款");
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = dto.getItems();
        if (items != null && !items.isEmpty()) {
            for (OrderItem it : items) {
                if (it.getGoodsId() != null) {
                    Goods g = goodsMapper.selectById(it.getGoodsId());
                    if (g != null && g.getCostPrice() != null) it.setCost(g.getCostPrice());
                }
                if (it.getPrice() != null && it.getQuantity() != null) {
                    it.setAmount(it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
                    total = total.add(it.getAmount());
                }
            }
        }
        main.setTotalAmount(total);
        orderMainMapper.insert(main);
        if (items != null) {
            for (OrderItem it : items) {
                it.setOrderId(main.getId());
                orderItemMapper.insert(it);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(OrderSaveDTO dto) {
        OrderMain main = dto.getOrder();
        if (main == null || main.getId() == null) throw new RuntimeException("订单ID不能为空");
        List<OrderItem> items = dto.getItems();
        BigDecimal total = BigDecimal.ZERO;
        if (items != null) {
            for (OrderItem it : items) {
                if (it.getGoodsId() != null) {
                    Goods g = goodsMapper.selectById(it.getGoodsId());
                    if (g != null && g.getCostPrice() != null) it.setCost(g.getCostPrice());
                }
                if (it.getPrice() != null && it.getQuantity() != null) {
                    it.setAmount(it.getPrice().multiply(BigDecimal.valueOf(it.getQuantity())));
                    total = total.add(it.getAmount());
                }
                it.setOrderId(main.getId());
            }
        }
        main.setTotalAmount(total);
        orderMainMapper.updateById(main);
        orderItemMapper.delete(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, main.getId()));
        if (items != null) {
            for (OrderItem it : items) {
                orderItemMapper.insert(it);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        orderItemMapper.delete(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id));
        orderMainMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, String status) {
        OrderMain main = orderMainMapper.selectById(id);
        if (main == null) return;
        String oldStatus = main.getStatus();
        // 已发货/已完成：出库扣减库存（仅当原状态未出库时执行一次）
        if ("已发货".equals(status) || "已完成".equals(status)) {
            if (!"已发货".equals(oldStatus) && !"已完成".equals(oldStatus)) {
                List<OrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, id));
                for (OrderItem it : items) {
                    if (it.getGoodsId() == null || it.getQuantity() == null || it.getQuantity() <= 0) continue;
                    Goods g = goodsMapper.selectById(it.getGoodsId());
                    if (g == null) continue;
                    int current = g.getStock() == null ? 0 : g.getStock();
                    int deduct = it.getQuantity();
                    if (deduct > current)
                        throw new RuntimeException("商品【" + g.getGoodsName() + "】库存不足，当前 " + current + "，需要 " + deduct);
                    int newStock = current - deduct;
                    g.setStock(newStock);
                    goodsMapper.updateById(g);
                    InventoryFlow flow = new InventoryFlow();
                    flow.setFlowNo("IF" + System.currentTimeMillis() + "_" + it.getId());
                    flow.setGoodsId(g.getId());
                    flow.setGoodsName(g.getGoodsName());
                    flow.setFlowType("出库");
                    flow.setBizType("正常出库");
                    flow.setQuantity(BigDecimal.valueOf(deduct));
                    flow.setAfterQuantity(BigDecimal.valueOf(newStock));
                    flow.setRefNo(main.getOrderNo());
                    flow.setFlowTime(LocalDateTime.now());
                    flow.setCreateTime(LocalDateTime.now());
                    inventoryFlowMapper.insert(flow);
                }
            }
        }
        // 待付款/已付款：根据流水表判断是否已扣减过库存，若已扣减则退库恢复
        if ("待付款".equals(status) || "已付款".equals(status)) {
            List<InventoryFlow> outFlows = inventoryFlowMapper.selectList(
                new LambdaQueryWrapper<InventoryFlow>()
                    .eq(InventoryFlow::getRefNo, main.getOrderNo())
                    .eq(InventoryFlow::getFlowType, "出库"));
            if (!outFlows.isEmpty()) {
                for (InventoryFlow out : outFlows) {
                    if (out.getGoodsId() == null || out.getQuantity() == null || out.getQuantity().signum() <= 0) continue;
                    Goods g = goodsMapper.selectById(out.getGoodsId());
                    if (g == null) continue;
                    int current = g.getStock() == null ? 0 : g.getStock();
                    int addBack = out.getQuantity().intValue();
                    int newStock = current + addBack;
                    g.setStock(newStock);
                    goodsMapper.updateById(g);
                    InventoryFlow backFlow = new InventoryFlow();
                    backFlow.setFlowNo("IF" + System.currentTimeMillis() + "_back_" + out.getId());
                    backFlow.setGoodsId(g.getId());
                    backFlow.setGoodsName(g.getGoodsName());
                    backFlow.setFlowType("退库");
                    backFlow.setBizType("退库");
                    backFlow.setQuantity(out.getQuantity());
                    backFlow.setAfterQuantity(BigDecimal.valueOf(newStock));
                    backFlow.setRefNo(main.getOrderNo());
                    backFlow.setFlowTime(LocalDateTime.now());
                    backFlow.setCreateTime(LocalDateTime.now());
                    inventoryFlowMapper.insert(backFlow);
                }
            }
        }
        OrderMain update = new OrderMain();
        update.setId(id);
        update.setStatus(status);
        orderMainMapper.updateById(update);
        if (main.getMemberId() != null && ("已完成".equals(status) || "已付款".equals(status))) {
            memberLifecycleService.updateLastConsumeTimeByOrder(main.getMemberId());
        }
    }
}
