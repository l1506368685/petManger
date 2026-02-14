package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.VaccineRecord;
import com.jsonll.base.request.VaccinePageRequest;

public interface VaccineRecordService {
    IPage<VaccineRecord> pageList(VaccinePageRequest request);
    void add(VaccineRecord record);
    void update(VaccineRecord record);
    void delete(Long id);
    VaccineRecord detail(Long id);
}
