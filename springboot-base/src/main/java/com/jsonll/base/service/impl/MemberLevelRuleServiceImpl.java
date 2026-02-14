package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.MemberLevelRule;
import com.jsonll.base.mapper.MemberLevelRuleMapper;
import com.jsonll.base.request.MemberLevelRulePageRequest;
import com.jsonll.base.service.MemberLevelRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MemberLevelRuleServiceImpl implements MemberLevelRuleService {

    @Autowired
    private MemberLevelRuleMapper memberLevelRuleMapper;

    @Override
    public IPage<MemberLevelRule> pageList(MemberLevelRulePageRequest request) {
        Page<MemberLevelRule> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<MemberLevelRule> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getLevelName())) wrapper.like(MemberLevelRule::getLevelName, request.getLevelName());
        wrapper.orderByAsc(MemberLevelRule::getSortOrder);
        return memberLevelRuleMapper.selectPage(page, wrapper);
    }

    @Override
    public List<MemberLevelRule> listAllEnabled() {
        LambdaQueryWrapper<MemberLevelRule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberLevelRule::getStatus, 1).orderByAsc(MemberLevelRule::getSortOrder);
        return memberLevelRuleMapper.selectList(wrapper);
    }

    @Override
    public void add(MemberLevelRule entity) {
        memberLevelRuleMapper.insert(entity);
    }

    @Override
    public void update(MemberLevelRule entity) {
        memberLevelRuleMapper.updateById(entity);
    }

    @Override
    public void delete(Long id) {
        memberLevelRuleMapper.deleteById(id);
    }

    @Override
    public MemberLevelRule detail(Long id) {
        return memberLevelRuleMapper.selectById(id);
    }
}
