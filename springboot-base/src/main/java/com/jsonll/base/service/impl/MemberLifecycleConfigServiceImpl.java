package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jsonll.base.entity.MemberLifecycleConfig;
import com.jsonll.base.mapper.MemberLifecycleConfigMapper;
import com.jsonll.base.service.MemberLifecycleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberLifecycleConfigServiceImpl implements MemberLifecycleConfigService {

    @Autowired
    private MemberLifecycleConfigMapper configMapper;

    @Override
    public List<MemberLifecycleConfig> listAll() {
        return configMapper.selectList(null);
    }

    @Override
    public String getValue(String configKey) {
        LambdaQueryWrapper<MemberLifecycleConfig> w = new LambdaQueryWrapper<>();
        w.eq(MemberLifecycleConfig::getConfigKey, configKey);
        MemberLifecycleConfig c = configMapper.selectOne(w);
        return c != null ? c.getConfigValue() : null;
    }

    @Override
    public int getIntValue(String configKey, int defaultValue) {
        String v = getValue(configKey);
        if (v == null || v.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public void saveOrUpdate(MemberLifecycleConfig config) {
        LambdaQueryWrapper<MemberLifecycleConfig> w = new LambdaQueryWrapper<>();
        w.eq(MemberLifecycleConfig::getConfigKey, config.getConfigKey());
        MemberLifecycleConfig exist = configMapper.selectOne(w);
        if (exist != null) {
            exist.setConfigValue(config.getConfigValue());
            exist.setRemark(config.getRemark());
            configMapper.updateById(exist);
        } else {
            configMapper.insert(config);
        }
    }
}
