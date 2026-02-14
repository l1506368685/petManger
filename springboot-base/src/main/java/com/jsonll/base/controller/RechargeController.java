package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.RechargeRecord;
import com.jsonll.base.request.RechargePageRequest;
import com.jsonll.base.service.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recharge")
public class RechargeController {

    @Autowired
    private RechargeRecordService rechargeRecordService;

    @GetMapping("/pageList")
    public R<?> pageList(RechargePageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(rechargeRecordService.pageList(request));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody RechargeRecord record) {
        rechargeRecordService.add(record);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<RechargeRecord> detail(@RequestParam("id") Long id) {
        return R.ok(rechargeRecordService.detail(id));
    }
}
