package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.MemberLifecycleWarning;
import com.jsonll.base.request.MemberLifecycleWarningPageRequest;

public interface MemberLifecycleWarningService {
    IPage<MemberLifecycleWarning> pageList(MemberLifecycleWarningPageRequest request);
}
