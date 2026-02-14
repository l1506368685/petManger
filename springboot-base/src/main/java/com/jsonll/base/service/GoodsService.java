package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.Goods;
import com.jsonll.base.request.GoodsPageRequest;

import java.util.List;

public interface GoodsService {
    IPage<Goods> pageList(GoodsPageRequest request);
    void add(Goods goods);
    void update(Goods goods);
    void updateStock(Long goodsId, Integer stock);
    void delete(Long id);
    Goods detail(Long id);
    List<Goods> listAll();
}
