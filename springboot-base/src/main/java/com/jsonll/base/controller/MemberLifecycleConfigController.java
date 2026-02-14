package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.MemberLifecycleConfig;
import com.jsonll.base.service.MemberLifecycleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memberLifecycleConfig")
public class MemberLifecycleConfigController {

    @Autowired
    private MemberLifecycleConfigService configService;

    @GetMapping("/list")
    public R<List<MemberLifecycleConfig>> list() {
        return R.ok(configService.listAll());
    }

    @PostMapping("/save")
    public R<Void> save(@RequestBody MemberLifecycleConfig config) {
        configService.saveOrUpdate(config);
        return R.ok();
    }
}
