package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.Goods;
import com.jsonll.base.mapper.GoodsMapper;
import com.jsonll.base.request.GoodsPageRequest;
import com.jsonll.base.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public IPage<Goods> pageList(GoodsPageRequest request) {
        Page<Goods> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getGoodsName())) wrapper.like(Goods::getGoodsName, request.getGoodsName());
        if (StringUtils.hasText(request.getGoodsCode())) wrapper.like(Goods::getGoodsCode, request.getGoodsCode());
        if (StringUtils.hasText(request.getBrand())) wrapper.like(Goods::getBrand, request.getBrand());
        if (StringUtils.hasText(request.getCategory())) wrapper.eq(Goods::getCategory, request.getCategory());
        wrapper.orderByDesc(Goods::getCreateTime);
        return goodsMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(Goods goods) {
        goodsMapper.insert(goods);
    }

    @Override
    public void update(Goods goods) {
        goodsMapper.updateById(goods);
    }

    @Override
    public void updateStock(Long goodsId, Integer stock) {
        Goods g = new Goods();
        g.setId(goodsId);
        g.setStock(stock != null ? stock : 0);
        goodsMapper.updateById(g);
    }

    @Override
    public void delete(Long id) {
        goodsMapper.deleteById(id);
    }

    @Override
    public Goods detail(Long id) {
        return goodsMapper.selectById(id);
    }

    @Override
    public List<Goods> listAll() {
        return goodsMapper.selectList(new LambdaQueryWrapper<Goods>().eq(Goods::getStatus, 1));
    }
}
