package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.StockCheckItem;
import com.jsonll.base.entity.StockCheckMain;
import com.jsonll.base.request.StockCheckPageRequest;

import java.util.List;
import java.util.Map;

public interface StockCheckService {
    IPage<StockCheckMain> pageList(StockCheckPageRequest request);
    Map<String, Object> detail(Long id);
    void add(StockCheckMain main);
    void update(StockCheckMain main, List<StockCheckItem> items);
    void delete(Long id);
    void confirm(Long id);
}
