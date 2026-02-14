package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.RechargeRecord;
import com.jsonll.base.request.RechargePageRequest;

public interface RechargeRecordService {
    IPage<RechargeRecord> pageList(RechargePageRequest request);
    void add(RechargeRecord record);
    RechargeRecord detail(Long id);
}
