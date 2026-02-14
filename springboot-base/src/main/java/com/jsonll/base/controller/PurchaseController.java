package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.PurchaseRecord;
import com.jsonll.base.request.PurchasePageRequest;
import com.jsonll.base.service.PurchaseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseRecordService purchaseRecordService;

    @GetMapping("/pageList")
    public R<?> pageList(PurchasePageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(purchaseRecordService.pageList(request));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody PurchaseRecord record) {
        purchaseRecordService.add(record);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody PurchaseRecord record) {
        purchaseRecordService.update(record);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        purchaseRecordService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<PurchaseRecord> detail(@RequestParam("id") Long id) {
        return R.ok(purchaseRecordService.detail(id));
    }

    /** 保质期预警：到期日在未来 days 天内的采购批次 */
    @GetMapping("/expiryAlertList")
    public R<List<PurchaseRecord>> expiryAlertList(@RequestParam(required = false) Integer days) {
        return R.ok(purchaseRecordService.expiryAlertList(days));
    }
}
