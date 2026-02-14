package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.Member;
import com.jsonll.base.entity.RechargeRecord;
import com.jsonll.base.mapper.MemberMapper;
import com.jsonll.base.mapper.RechargeRecordMapper;
import com.jsonll.base.request.RechargePageRequest;
import com.jsonll.base.service.MemberLifecycleService;
import com.jsonll.base.service.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class RechargeRecordServiceImpl implements RechargeRecordService {

    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberLifecycleService memberLifecycleService;

    @Override
    public IPage<RechargeRecord> pageList(RechargePageRequest request) {
        Page<RechargeRecord> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<RechargeRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getOrderNo())) wrapper.like(RechargeRecord::getOrderNo, request.getOrderNo());
        if (StringUtils.hasText(request.getMemberName())) wrapper.like(RechargeRecord::getMemberName, request.getMemberName());
        if (StringUtils.hasText(request.getPhone())) wrapper.like(RechargeRecord::getPhone, request.getPhone());
        if (request.getStartTime() != null) wrapper.ge(RechargeRecord::getCreateTime, request.getStartTime());
        if (request.getEndTime() != null) wrapper.le(RechargeRecord::getCreateTime, request.getEndTime());
        wrapper.orderByDesc(RechargeRecord::getCreateTime);
        return rechargeRecordMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(RechargeRecord record) {
        if (record.getOrderNo() == null || record.getOrderNo().isEmpty()) {
            record.setOrderNo("R" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4));
        }
        if (record.getMemberId() != null) {
            Member m = memberMapper.selectById(record.getMemberId());
            if (m != null) {
                record.setMemberName(m.getName());
                record.setPhone(m.getPhone());
                BigDecimal amt = record.getAmount() != null ? record.getAmount() : BigDecimal.ZERO;
                BigDecimal cur = m.getBalance() != null ? m.getBalance() : BigDecimal.ZERO;
                m.setBalance(cur.add(amt));
                memberMapper.updateById(m);
            }
            memberLifecycleService.updateLastConsumeTimeByRecharge(record.getMemberId());
        }
        rechargeRecordMapper.insert(record);
    }

    @Override
    public RechargeRecord detail(Long id) {
        return rechargeRecordMapper.selectById(id);
    }
}
