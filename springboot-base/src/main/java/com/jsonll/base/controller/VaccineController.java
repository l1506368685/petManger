package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.VaccineRecord;
import com.jsonll.base.request.VaccinePageRequest;
import com.jsonll.base.service.VaccineRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vaccine")
public class VaccineController {

    @Autowired
    private VaccineRecordService vaccineRecordService;

    @GetMapping("/pageList")
    public R<?> pageList(VaccinePageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(vaccineRecordService.pageList(request));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody VaccineRecord record) {
        vaccineRecordService.add(record);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody VaccineRecord record) {
        vaccineRecordService.update(record);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        vaccineRecordService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<VaccineRecord> detail(@RequestParam("id") Long id) {
        return R.ok(vaccineRecordService.detail(id));
    }
}
