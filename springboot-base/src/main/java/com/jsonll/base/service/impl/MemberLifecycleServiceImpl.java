package com.jsonll.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jsonll.base.entity.*;
import com.jsonll.base.mapper.*;
import com.jsonll.base.service.MemberLifecycleConfigService;
import com.jsonll.base.service.MemberLifecycleService;
import com.jsonll.base.service.MemberLevelRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberLifecycleServiceImpl implements MemberLifecycleService {

    private static final String TAG_SLEEPING = "沉睡";
    private static final String CONFIG_CHURN_DAYS = "churn_warning_days";
    private static final String CONFIG_SLEEPING_DAYS = "sleeping_register_days";

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private OrderMainMapper orderMainMapper;
    @Autowired
    private MemberLevelRuleService memberLevelRuleService;
    @Autowired
    private MemberLifecycleConfigService configService;
    @Autowired
    private MemberLifecycleWarningMapper warningMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeMonthlyLevelCalc() {
        LocalDateTime since = LocalDateTime.now().minusMonths(12);
        List<MemberConsumeSum> sums = orderMainMapper.sumAmountByMemberSince(since);
        if (sums == null || sums.isEmpty()) return;
        List<MemberLevelRule> rules = memberLevelRuleService.listAllEnabled();
        if (rules == null || rules.isEmpty()) return;
        for (MemberConsumeSum sum : sums) {
            Member member = memberMapper.selectById(sum.getMemberId());
            if (member == null) continue;
            String levelName = resolveLevel(sum.getTotalAmount(), rules);
            if (levelName != null && !levelName.equals(member.getLevel())) {
                member.setLevel(levelName);
                memberMapper.updateById(member);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeChurnWarningScan() {
        int days = configService.getIntValue(CONFIG_CHURN_DAYS, 90);
        LocalDateTime deadline = LocalDateTime.now().minusDays(days);
        // 删除今日已生成的流失预警，再重新生成
        LambdaQueryWrapper<MemberLifecycleWarning> del = new LambdaQueryWrapper<>();
        del.eq(MemberLifecycleWarning::getWarningType, "CHURN")
          .apply("DATE(create_time) = CURDATE()");
        warningMapper.delete(del);
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNotNull(Member::getLastConsumeTime).lt(Member::getLastConsumeTime, deadline);
        List<Member> list = memberMapper.selectList(wrapper);
        for (Member m : list) {
            MemberLifecycleWarning w = new MemberLifecycleWarning();
            w.setMemberId(m.getId());
            w.setMemberName(m.getName());
            w.setPhone(m.getPhone());
            w.setWarningType("CHURN");
            w.setLastConsumeTime(m.getLastConsumeTime());
            warningMapper.insert(w);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeSleepingScan() {
        int registerDays = configService.getIntValue(CONFIG_SLEEPING_DAYS, 30);
        LocalDateTime registerDeadline = LocalDateTime.now().minusDays(registerDays);
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.lt(Member::getCreateTime, registerDeadline)
               .and(w -> w.isNull(Member::getLastConsumeTime));
        List<Member> list = memberMapper.selectList(wrapper);
        // 删除今日已生成的沉睡预警
        LambdaQueryWrapper<MemberLifecycleWarning> del = new LambdaQueryWrapper<>();
        del.eq(MemberLifecycleWarning::getWarningType, "SLEEPING")
          .apply("DATE(create_time) = CURDATE()");
        warningMapper.delete(del);
        for (Member m : list) {
            long days = java.time.temporal.ChronoUnit.DAYS.between(m.getCreateTime().toLocalDate(), java.time.LocalDate.now());
            addSleepingTag(m);
            MemberLifecycleWarning w = new MemberLifecycleWarning();
            w.setMemberId(m.getId());
            w.setMemberName(m.getName());
            w.setPhone(m.getPhone());
            w.setWarningType("SLEEPING");
            w.setRegisterDays((int) days);
            warningMapper.insert(w);
        }
    }

    @Override
    public void updateLastConsumeTimeByOrder(Long memberId) {
        if (memberId == null) return;
        LambdaUpdateWrapper<Member> u = new LambdaUpdateWrapper<>();
        u.eq(Member::getId, memberId).set(Member::getLastConsumeTime, LocalDateTime.now());
        memberMapper.update(null, u);
    }

    @Override
    public void updateLastConsumeTimeByRecharge(Long memberId) {
        if (memberId == null) return;
        LambdaUpdateWrapper<Member> u = new LambdaUpdateWrapper<>();
        u.eq(Member::getId, memberId).set(Member::getLastConsumeTime, LocalDateTime.now());
        memberMapper.update(null, u);
    }

    private String resolveLevel(BigDecimal totalAmount, List<MemberLevelRule> rules) {
        if (totalAmount == null) totalAmount = BigDecimal.ZERO;
        for (MemberLevelRule r : rules) {
            if (totalAmount.compareTo(r.getMinAmount()) >= 0 && totalAmount.compareTo(r.getMaxAmount()) <= 0)
                return r.getLevelName();
        }
        return null;
    }

    private void addSleepingTag(Member member) {
        List<String> tags = parseTags(member.getTags());
        if (tags.contains(TAG_SLEEPING)) return;
        tags.add(TAG_SLEEPING);
        member.setTags(JSON.toJSONString(tags));
        memberMapper.updateById(member);
    }

    private static List<String> parseTags(String tagsJson) {
        List<String> list = new ArrayList<>();
        if (tagsJson != null && !tagsJson.trim().isEmpty()) {
            try {
                List<String> parsed = JSON.parseArray(tagsJson, String.class);
                if (parsed != null) list.addAll(parsed);
            } catch (Exception ignored) {}
        }
        return list;
    }
}
