package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.CardTransaction;
import com.jsonll.base.request.CardTransactionPageRequest;
import com.jsonll.base.service.CardTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cardTransaction")
public class CardTransactionController {

    @Autowired
    private CardTransactionService cardTransactionService;

    @GetMapping("/pageList")
    public R<?> pageList(CardTransactionPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(cardTransactionService.pageList(request));
    }

    @GetMapping("/detail")
    public R<CardTransaction> detail(@RequestParam("id") Long id) {
        return R.ok(cardTransactionService.detail(id));
    }
}
