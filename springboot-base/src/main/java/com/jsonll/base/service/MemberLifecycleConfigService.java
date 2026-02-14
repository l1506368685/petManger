package com.jsonll.base.service;

import com.jsonll.base.entity.MemberLifecycleConfig;

import java.util.List;

public interface MemberLifecycleConfigService {
    List<MemberLifecycleConfig> listAll();
    String getValue(String configKey);
    int getIntValue(String configKey, int defaultValue);
    void saveOrUpdate(MemberLifecycleConfig config);
}
