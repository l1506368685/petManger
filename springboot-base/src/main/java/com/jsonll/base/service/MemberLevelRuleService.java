package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.MemberLevelRule;
import com.jsonll.base.request.MemberLevelRulePageRequest;

import java.util.List;

public interface MemberLevelRuleService {
    IPage<MemberLevelRule> pageList(MemberLevelRulePageRequest request);
    List<MemberLevelRule> listAllEnabled();
    void add(MemberLevelRule entity);
    void update(MemberLevelRule entity);
    void delete(Long id);
    MemberLevelRule detail(Long id);
}
