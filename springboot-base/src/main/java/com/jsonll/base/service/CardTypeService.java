package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.CardType;
import com.jsonll.base.request.CardTypePageRequest;

import java.util.List;

public interface CardTypeService {
    IPage<CardType> pageList(CardTypePageRequest request);
    List<CardType> listAll(Integer status);
    void add(CardType cardType);
    void update(CardType cardType);
    void delete(Long id);
    CardType detail(Long id);
}
