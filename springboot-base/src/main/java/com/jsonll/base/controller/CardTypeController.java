package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.CardType;
import com.jsonll.base.request.CardTypePageRequest;
import com.jsonll.base.service.CardTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardType")
public class CardTypeController {

    @Autowired
    private CardTypeService cardTypeService;

    @GetMapping("/pageList")
    public R<?> pageList(CardTypePageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(cardTypeService.pageList(request));
    }

    @GetMapping("/listAll")
    public R<List<CardType>> listAll(@RequestParam(value = "status", required = false) Integer status) {
        return R.ok(cardTypeService.listAll(status));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody CardType cardType) {
        cardTypeService.add(cardType);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody CardType cardType) {
        cardTypeService.update(cardType);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        cardTypeService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<CardType> detail(@RequestParam("id") Long id) {
        return R.ok(cardTypeService.detail(id));
    }
}
