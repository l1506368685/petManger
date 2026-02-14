package com.jsonll.base.task;

import com.jsonll.base.service.MemberLifecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 会员生命周期定时任务：每月等级计算、每日流失预警与沉睡扫描。
 */
@Component
public class MemberLifecycleTask {

    @Autowired
    private MemberLifecycleService memberLifecycleService;

    /** 每月 1 日 凌晨 2 点 执行等级升降级 */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void monthlyLevelCalc() {
        memberLifecycleService.executeMonthlyLevelCalc();
    }

    /** 每日 凌晨 3 点 执行流失预警扫描 */
    @Scheduled(cron = "0 0 3 * * ?")
    public void churnWarningScan() {
        memberLifecycleService.executeChurnWarningScan();
    }

    /** 每日 凌晨 3 点 10 分 执行沉睡会员扫描 */
    @Scheduled(cron = "0 10 3 * * ?")
    public void sleepingScan() {
        memberLifecycleService.executeSleepingScan();
    }
}
