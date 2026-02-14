package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.CardType;
import com.jsonll.base.mapper.CardTypeMapper;
import com.jsonll.base.request.CardTypePageRequest;
import com.jsonll.base.service.CardTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CardTypeServiceImpl implements CardTypeService {

    @Autowired
    private CardTypeMapper cardTypeMapper;

    @Override
    public IPage<CardType> pageList(CardTypePageRequest request) {
        Page<CardType> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<CardType> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getTypeName())) wrapper.like(CardType::getTypeName, request.getTypeName());
        if (request.getCardKind() != null) wrapper.eq(CardType::getCardKind, request.getCardKind());
        if (request.getStatus() != null) wrapper.eq(CardType::getStatus, request.getStatus());
        wrapper.orderByAsc(CardType::getSort).orderByDesc(CardType::getCreateTime);
        return cardTypeMapper.selectPage(page, wrapper);
    }

    @Override
    public List<CardType> listAll(Integer status) {
        LambdaQueryWrapper<CardType> wrapper = new LambdaQueryWrapper<>();
        if (status != null) wrapper.eq(CardType::getStatus, status);
        wrapper.orderByAsc(CardType::getSort);
        return cardTypeMapper.selectList(wrapper);
    }

    @Override
    public void add(CardType cardType) {
        cardTypeMapper.insert(cardType);
    }

    @Override
    public void update(CardType cardType) {
        cardTypeMapper.updateById(cardType);
    }

    @Override
    public void delete(Long id) {
        cardTypeMapper.deleteById(id);
    }

    @Override
    public CardType detail(Long id) {
        return cardTypeMapper.selectById(id);
    }
}
