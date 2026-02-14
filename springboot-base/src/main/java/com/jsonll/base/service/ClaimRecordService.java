package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.ClaimRecord;
import com.jsonll.base.request.ClaimPageRequest;

public interface ClaimRecordService {
    IPage<ClaimRecord> pageList(ClaimPageRequest request);
    void add(ClaimRecord record);
    void update(ClaimRecord record);
    void delete(Long id);
    ClaimRecord detail(Long id);
}
