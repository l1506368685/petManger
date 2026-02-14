package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.ClaimRecord;
import com.jsonll.base.request.ClaimPageRequest;
import com.jsonll.base.service.ClaimRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimRecordService claimRecordService;

    @GetMapping("/pageList")
    public R<?> pageList(ClaimPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(claimRecordService.pageList(request));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody ClaimRecord record) {
        claimRecordService.add(record);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody ClaimRecord record) {
        claimRecordService.update(record);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        claimRecordService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<ClaimRecord> detail(@RequestParam("id") Long id) {
        return R.ok(claimRecordService.detail(id));
    }
}
