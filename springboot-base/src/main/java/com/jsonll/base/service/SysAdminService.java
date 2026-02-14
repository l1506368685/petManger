package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.SysAdmin;
import com.jsonll.base.request.AdminPageRequest;

import java.util.Map;

public interface SysAdminService {

    Map<String, Object> login(String username, String password);

    IPage<SysAdmin> pageList(AdminPageRequest request);

    void add(SysAdmin admin);

    void update(SysAdmin admin);

    void delete(Long id);

    SysAdmin detail(Long id);

    void resetPwd(Long id, String newPassword);
}
