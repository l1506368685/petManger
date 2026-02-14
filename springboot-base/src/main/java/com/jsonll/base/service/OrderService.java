package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.OrderMain;
import com.jsonll.base.request.OrderPageRequest;
import com.jsonll.base.request.OrderSaveDTO;

import java.util.Map;

public interface OrderService {
    IPage<OrderMain> pageList(OrderPageRequest request);
    Map<String, Object> detail(Long id);
    void add(OrderSaveDTO dto);
    void update(OrderSaveDTO dto);
    void delete(Long id);
    void updateStatus(Long id, String status);
}
