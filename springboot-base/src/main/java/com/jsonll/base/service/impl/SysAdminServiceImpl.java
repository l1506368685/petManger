package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.SysAdmin;
import com.jsonll.base.mapper.SysAdminMapper;
import com.jsonll.base.request.AdminPageRequest;
import com.jsonll.base.service.SysAdminService;
import com.jsonll.base.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class SysAdminServiceImpl implements SysAdminService {

    @Autowired
    private SysAdminMapper sysAdminMapper;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(String username, String password) {
        LambdaQueryWrapper<SysAdmin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAdmin::getUsername, username);
        SysAdmin admin = sysAdminMapper.selectOne(wrapper);
        if (admin == null) {
            throw new RuntimeException("账号不存在");
        }
        if (admin.getStatus() != null && admin.getStatus() == 0) {
            throw new RuntimeException("账号已禁用");
        }
        if (!password.equals(admin.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        String token = jwtUtil.generateToken(admin.getId(), admin.getUsername(), admin.getRole() != null ? admin.getRole() : "");
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", admin.getId());
        result.put("username", admin.getUsername());
        result.put("name", admin.getName());
        result.put("role", admin.getRole());
        return result;
    }

    @Override
    public IPage<SysAdmin> pageList(AdminPageRequest request) {
        Page<SysAdmin> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<SysAdmin> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getUsername())) {
            wrapper.like(SysAdmin::getUsername, request.getUsername());
        }
        if (StringUtils.hasText(request.getName())) {
            wrapper.like(SysAdmin::getName, request.getName());
        }
        if (StringUtils.hasText(request.getRole())) {
            wrapper.eq(SysAdmin::getRole, request.getRole());
        }
        wrapper.orderByDesc(SysAdmin::getCreateTime);
        return sysAdminMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(SysAdmin admin) {
        LambdaQueryWrapper<SysAdmin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAdmin::getUsername, admin.getUsername());
        if (sysAdminMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("账号已存在");
        }
        sysAdminMapper.insert(admin);
    }

    @Override
    public void update(SysAdmin admin) {
        sysAdminMapper.updateById(admin);
    }

    @Override
    public void delete(Long id) {
        sysAdminMapper.deleteById(id);
    }

    @Override
    public SysAdmin detail(Long id) {
        SysAdmin admin = sysAdminMapper.selectById(id);
        if (admin != null) {
            admin.setPassword(null);
        }
        return admin;
    }

    @Override
    public void resetPwd(Long id, String newPassword) {
        SysAdmin admin = new SysAdmin();
        admin.setId(id);
        admin.setPassword(newPassword);
        sysAdminMapper.updateById(admin);
    }
}
