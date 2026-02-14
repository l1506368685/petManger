package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.Goods;
import com.jsonll.base.request.GoodsPageRequest;
import com.jsonll.base.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/pageList")
    public R<?> pageList(GoodsPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(goodsService.pageList(request));
    }

    @GetMapping("/listAll")
    public R<List<Goods>> listAll() {
        return R.ok(goodsService.listAll());
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody Goods goods) {
        goodsService.add(goods);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody Goods goods) {
        goodsService.update(goods);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        goodsService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<Goods> detail(@RequestParam("id") Long id) {
        return R.ok(goodsService.detail(id));
    }
}
