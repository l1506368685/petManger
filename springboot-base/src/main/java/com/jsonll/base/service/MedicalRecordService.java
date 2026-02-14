package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.MedicalRecord;
import com.jsonll.base.request.MedicalPageRequest;

public interface MedicalRecordService {
    IPage<MedicalRecord> pageList(MedicalPageRequest request);
    void add(MedicalRecord record);
    void update(MedicalRecord record);
    void delete(Long id);
    MedicalRecord detail(Long id);
}
