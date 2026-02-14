package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.CardTransaction;
import com.jsonll.base.mapper.CardTransactionMapper;
import com.jsonll.base.request.CardTransactionPageRequest;
import com.jsonll.base.service.CardTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CardTransactionServiceImpl implements CardTransactionService {

    @Autowired
    private CardTransactionMapper cardTransactionMapper;

    @Override
    public IPage<CardTransaction> pageList(CardTransactionPageRequest request) {
        Page<CardTransaction> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<CardTransaction> wrapper = new LambdaQueryWrapper<>();
        if (request.getCardId() != null) wrapper.eq(CardTransaction::getCardId, request.getCardId());
        if (request.getMemberId() != null) wrapper.eq(CardTransaction::getMemberId, request.getMemberId());
        if (StringUtils.hasText(request.getTransType())) wrapper.eq(CardTransaction::getTransType, request.getTransType());
        if (request.getStartTime() != null) wrapper.ge(CardTransaction::getCreateTime, request.getStartTime());
        if (request.getEndTime() != null) wrapper.le(CardTransaction::getCreateTime, request.getEndTime());
        wrapper.orderByDesc(CardTransaction::getCreateTime);
        return cardTransactionMapper.selectPage(page, wrapper);
    }

    @Override
    public CardTransaction detail(Long id) {
        return cardTransactionMapper.selectById(id);
    }
}
