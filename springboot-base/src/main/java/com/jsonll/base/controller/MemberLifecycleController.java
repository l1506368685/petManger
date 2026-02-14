package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.service.MemberLifecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会员生命周期：手动触发定时任务（用于测试或补跑）。
 */
@RestController
@RequestMapping("/memberLifecycle")
public class MemberLifecycleController {

    @Autowired
    private MemberLifecycleService memberLifecycleService;

    @PostMapping("/runLevelCalc")
    public R<Void> runLevelCalc() {
        memberLifecycleService.executeMonthlyLevelCalc();
        return R.ok();
    }

    @PostMapping("/runChurnScan")
    public R<Void> runChurnScan() {
        memberLifecycleService.executeChurnWarningScan();
        return R.ok();
    }

    @PostMapping("/runSleepingScan")
    public R<Void> runSleepingScan() {
        memberLifecycleService.executeSleepingScan();
        return R.ok();
    }
}
