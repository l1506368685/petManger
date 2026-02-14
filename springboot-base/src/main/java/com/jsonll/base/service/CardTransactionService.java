package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.CardTransaction;
import com.jsonll.base.request.CardTransactionPageRequest;

public interface CardTransactionService {
    IPage<CardTransaction> pageList(CardTransactionPageRequest request);
    CardTransaction detail(Long id);
}
