package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.MedicalRecord;
import com.jsonll.base.request.MedicalPageRequest;
import com.jsonll.base.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medical")
public class MedicalController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping("/pageList")
    public R<?> pageList(MedicalPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(medicalRecordService.pageList(request));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody MedicalRecord record) {
        medicalRecordService.add(record);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody MedicalRecord record) {
        medicalRecordService.update(record);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        medicalRecordService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<MedicalRecord> detail(@RequestParam("id") Long id) {
        return R.ok(medicalRecordService.detail(id));
    }
}
