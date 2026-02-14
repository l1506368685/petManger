package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.Member;
import com.jsonll.base.request.MemberPageRequest;

public interface MemberService {
    IPage<Member> pageList(MemberPageRequest request);
    void add(Member member);
    void update(Member member);
    void delete(Long id);
    Member detail(Long id);
}
