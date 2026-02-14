package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.ClaimRecord;
import com.jsonll.base.mapper.ClaimRecordMapper;
import com.jsonll.base.request.ClaimPageRequest;
import com.jsonll.base.service.ClaimRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
public class ClaimRecordServiceImpl implements ClaimRecordService {

    @Autowired
    private ClaimRecordMapper claimRecordMapper;

    @Override
    public IPage<ClaimRecord> pageList(ClaimPageRequest request) {
        Page<ClaimRecord> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<ClaimRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getClaimNo())) wrapper.like(ClaimRecord::getClaimNo, request.getClaimNo());
        if (StringUtils.hasText(request.getPetName())) wrapper.like(ClaimRecord::getPetName, request.getPetName());
        if (StringUtils.hasText(request.getReason())) wrapper.like(ClaimRecord::getReason, request.getReason());
        if (request.getStartDate() != null) wrapper.ge(ClaimRecord::getEventDate, request.getStartDate());
        if (request.getEndDate() != null) wrapper.le(ClaimRecord::getEventDate, request.getEndDate());
        wrapper.orderByDesc(ClaimRecord::getCreateTime);
        return claimRecordMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(ClaimRecord record) {
        if (record.getClaimNo() == null || record.getClaimNo().isEmpty()) {
            record.setClaimNo("C" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4));
        }
        claimRecordMapper.insert(record);
    }

    @Override
    public void update(ClaimRecord record) {
        claimRecordMapper.updateById(record);
    }

    @Override
    public void delete(Long id) {
        claimRecordMapper.deleteById(id);
    }

    @Override
    public ClaimRecord detail(Long id) {
        return claimRecordMapper.selectById(id);
    }
}
