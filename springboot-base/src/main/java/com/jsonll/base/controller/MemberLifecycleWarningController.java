package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.request.MemberLifecycleWarningPageRequest;
import com.jsonll.base.service.MemberLifecycleWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/memberLifecycleWarning")
public class MemberLifecycleWarningController {

    @Autowired
    private MemberLifecycleWarningService warningService;

    @GetMapping("/pageList")
    public R<?> pageList(MemberLifecycleWarningPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(warningService.pageList(request));
    }
}
