package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.StockCheckMain;
import com.jsonll.base.request.StockCheckPageRequest;
import com.jsonll.base.request.StockCheckUpdateDTO;
import com.jsonll.base.service.StockCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stockCheck")
public class StockCheckController {

    @Autowired
    private StockCheckService stockCheckService;

    @GetMapping("/pageList")
    public R<?> pageList(StockCheckPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(stockCheckService.pageList(request));
    }

    @GetMapping("/detail")
    public R<?> detail(@RequestParam("id") Long id) {
        return R.ok(stockCheckService.detail(id));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody StockCheckMain main) {
        stockCheckService.add(main);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody StockCheckUpdateDTO dto) {
        if (dto.getMain() == null) throw new RuntimeException("盘点单不能为空");
        stockCheckService.update(dto.getMain(), dto.getItems());
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        stockCheckService.delete(id);
        return R.ok();
    }

    @PutMapping("/confirm")
    public R<Void> confirm(@RequestParam("id") Long id) {
        stockCheckService.confirm(id);
        return R.ok();
    }
}
