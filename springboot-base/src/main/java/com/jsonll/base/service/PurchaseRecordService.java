package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.PurchaseRecord;
import com.jsonll.base.request.PurchasePageRequest;

import java.util.List;

public interface PurchaseRecordService {
    IPage<PurchaseRecord> pageList(PurchasePageRequest request);
    void add(PurchaseRecord record);
    void update(PurchaseRecord record);
    void delete(Long id);
    PurchaseRecord detail(Long id);
    /** 保质期预警：到期日在未来 days 天内的采购批次，按到期日升序 */
    List<PurchaseRecord> expiryAlertList(Integer days);
}
