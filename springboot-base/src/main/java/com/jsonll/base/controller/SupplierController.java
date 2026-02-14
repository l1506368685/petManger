package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.Supplier;
import com.jsonll.base.request.SupplierPageRequest;
import com.jsonll.base.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/pageList")
    public R<?> pageList(SupplierPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(supplierService.pageList(request));
    }

    @GetMapping("/listAll")
    public R<List<Supplier>> listAll(@RequestParam(value = "status", required = false) Integer status) {
        return R.ok(supplierService.listAll(status));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody Supplier supplier) {
        supplierService.add(supplier);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody Supplier supplier) {
        supplierService.update(supplier);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        supplierService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<Supplier> detail(@RequestParam("id") Long id) {
        return R.ok(supplierService.detail(id));
    }
}
