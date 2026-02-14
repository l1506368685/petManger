package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.request.OrderPageRequest;
import com.jsonll.base.request.OrderSaveDTO;
import com.jsonll.base.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/pageList")
    public R<?> pageList(OrderPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(orderService.pageList(request));
    }

    @GetMapping("/detail")
    public R<Map<String, Object>> detail(@RequestParam("id") Long id) {
        return R.ok(orderService.detail(id));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody OrderSaveDTO dto) {
        orderService.add(dto);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody OrderSaveDTO dto) {
        orderService.update(dto);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        orderService.delete(id);
        return R.ok();
    }

    @PutMapping("/updateStatus")
    public R<Void> updateStatus(@RequestParam("id") Long id, @RequestParam("status") String status) {
        orderService.updateStatus(id, status);
        return R.ok();
    }
}
