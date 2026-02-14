package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.MemberLifecycleWarning;
import com.jsonll.base.mapper.MemberLifecycleWarningMapper;
import com.jsonll.base.request.MemberLifecycleWarningPageRequest;
import com.jsonll.base.service.MemberLifecycleWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MemberLifecycleWarningServiceImpl implements MemberLifecycleWarningService {

    @Autowired
    private MemberLifecycleWarningMapper warningMapper;

    @Override
    public IPage<MemberLifecycleWarning> pageList(MemberLifecycleWarningPageRequest request) {
        Page<MemberLifecycleWarning> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<MemberLifecycleWarning> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getWarningType())) wrapper.eq(MemberLifecycleWarning::getWarningType, request.getWarningType());
        wrapper.orderByDesc(MemberLifecycleWarning::getCreateTime);
        return warningMapper.selectPage(page, wrapper);
    }
}
