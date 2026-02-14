package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.SysDictItem;
import com.jsonll.base.entity.SysDictType;
import com.jsonll.base.request.DictItemPageRequest;
import com.jsonll.base.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private SysDictService sysDictService;

    @GetMapping("/typeList")
    public R<List<SysDictType>> typeList() {
        return R.ok(sysDictService.typeList());
    }

    @PostMapping("/type/add")
    public R<Void> addType(@RequestBody SysDictType type) {
        sysDictService.addType(type);
        return R.ok();
    }

    @PutMapping("/type/update")
    public R<Void> updateType(@RequestBody SysDictType type) {
        sysDictService.updateType(type);
        return R.ok();
    }

    @DeleteMapping("/type/delete")
    public R<Void> deleteType(@RequestParam("id") Long id) {
        sysDictService.deleteType(id);
        return R.ok();
    }

    @GetMapping("/items")
    public R<List<SysDictItem>> items(@RequestParam("dictType") String dictType) {
        return R.ok(sysDictService.itemsByType(dictType));
    }

    @GetMapping("/item/pageList")
    public R<?> itemPageList(DictItemPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(sysDictService.itemPageList(request));
    }

    @PostMapping("/item/add")
    public R<Void> addItem(@RequestBody SysDictItem item) {
        sysDictService.addItem(item);
        return R.ok();
    }

    @PutMapping("/item/update")
    public R<Void> updateItem(@RequestBody SysDictItem item) {
        sysDictService.updateItem(item);
        return R.ok();
    }

    @DeleteMapping("/item/delete")
    public R<Void> deleteItem(@RequestParam("id") Long id) {
        sysDictService.deleteItem(id);
        return R.ok();
    }

    @GetMapping("/item/detail")
    public R<SysDictItem> itemDetail(@RequestParam("id") Long id) {
        return R.ok(sysDictService.itemDetail(id));
    }
}
