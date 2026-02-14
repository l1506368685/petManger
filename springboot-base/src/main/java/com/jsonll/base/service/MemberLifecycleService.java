package com.jsonll.base.service;

/** 会员生命周期：定时任务与最后消费时间更新 */
public interface MemberLifecycleService {
    /** 每月1日执行：按近12个月消费总额自动升降级 */
    void executeMonthlyLevelCalc();
    /** 每日执行：流失预警扫描，写入 member_lifecycle_warning */
    void executeChurnWarningScan();
    /** 每日执行：沉睡会员扫描，写入 member_lifecycle_warning 并更新会员 tags */
    void executeSleepingScan();
    /** 订单完成时更新会员最后消费时间 */
    void updateLastConsumeTimeByOrder(Long memberId);
    /** 充值时更新会员最后消费时间（充值视为一次互动） */
    void updateLastConsumeTimeByRecharge(Long memberId);
}
