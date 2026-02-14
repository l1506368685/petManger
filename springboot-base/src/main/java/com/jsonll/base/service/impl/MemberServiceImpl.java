package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.Member;
import com.jsonll.base.mapper.MemberMapper;
import com.jsonll.base.request.MemberPageRequest;
import com.jsonll.base.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public IPage<Member> pageList(MemberPageRequest request) {
        Page<Member> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getName())) wrapper.like(Member::getName, request.getName());
        if (StringUtils.hasText(request.getPhone())) wrapper.like(Member::getPhone, request.getPhone());
        if (StringUtils.hasText(request.getMemberNo())) wrapper.like(Member::getMemberNo, request.getMemberNo());
        wrapper.orderByDesc(Member::getCreateTime);
        return memberMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(Member member) {
        memberMapper.insert(member);
    }

    @Override
    public void update(Member member) {
        memberMapper.updateById(member);
    }

    @Override
    public void delete(Long id) {
        memberMapper.deleteById(id);
    }

    @Override
    public Member detail(Long id) {
        return memberMapper.selectById(id);
    }
}
