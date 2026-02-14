package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.SysAdmin;
import com.jsonll.base.request.AdminPageRequest;
import com.jsonll.base.service.SysAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SysAdminService sysAdminService;

    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        if (username == null || password == null) {
            return R.fail("账号和密码不能为空");
        }
        try {
            Map<String, Object> result = sysAdminService.login(username, password);
            return R.ok(result);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @GetMapping("/pageList")
    public R<?> pageList(AdminPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(sysAdminService.pageList(request));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody SysAdmin admin) {
        try {
            sysAdminService.add(admin);
            return R.ok();
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody SysAdmin admin) {
        sysAdminService.update(admin);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        sysAdminService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<SysAdmin> detail(@RequestParam("id") Long id) {
        return R.ok(sysAdminService.detail(id));
    }

    @PutMapping("/resetPwd")
    public R<Void> resetPwd(@RequestParam("id") Long id, @RequestParam("newPassword") String newPassword) {
        sysAdminService.resetPwd(id, newPassword);
        return R.ok();
    }
}
