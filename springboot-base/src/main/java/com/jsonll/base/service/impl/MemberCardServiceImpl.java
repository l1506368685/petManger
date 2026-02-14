package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.CardTransaction;
import com.jsonll.base.entity.CardType;
import com.jsonll.base.entity.Member;
import com.jsonll.base.entity.MemberCard;
import com.jsonll.base.mapper.CardTransactionMapper;
import com.jsonll.base.mapper.CardTypeMapper;
import com.jsonll.base.mapper.MemberCardMapper;
import com.jsonll.base.mapper.MemberMapper;
import com.jsonll.base.request.CardBuyDTO;
import com.jsonll.base.request.CardConsumeDTO;
import com.jsonll.base.request.CardRechargeDTO;
import com.jsonll.base.request.MemberCardPageRequest;
import com.jsonll.base.service.MemberCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MemberCardServiceImpl implements MemberCardService {

    @Autowired
    private MemberCardMapper memberCardMapper;
    @Autowired
    private CardTypeMapper cardTypeMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private CardTransactionMapper cardTransactionMapper;

    @Override
    public IPage<MemberCard> pageList(MemberCardPageRequest request) {
        Page<MemberCard> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<MemberCard> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getCardNo())) wrapper.like(MemberCard::getCardNo, request.getCardNo());
        if (request.getMemberId() != null) wrapper.eq(MemberCard::getMemberId, request.getMemberId());
        if (StringUtils.hasText(request.getMemberName())) wrapper.like(MemberCard::getMemberName, request.getMemberName());
        if (request.getCardTypeId() != null) wrapper.eq(MemberCard::getCardTypeId, request.getCardTypeId());
        if (request.getCardKind() != null) wrapper.eq(MemberCard::getCardKind, request.getCardKind());
        if (request.getStatus() != null) wrapper.eq(MemberCard::getStatus, request.getStatus());
        wrapper.orderByDesc(MemberCard::getCreateTime);
        return memberCardMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(MemberCard memberCard) {
        memberCardMapper.insert(memberCard);
    }

    @Override
    public void update(MemberCard memberCard) {
        memberCardMapper.updateById(memberCard);
    }

    @Override
    public void delete(Long id) {
        memberCardMapper.deleteById(id);
    }

    @Override
    public MemberCard detail(Long id) {
        return memberCardMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MemberCard buyCard(CardBuyDTO dto) {
        CardType ct = cardTypeMapper.selectById(dto.getCardTypeId());
        if (ct == null) throw new RuntimeException("卡类型不存在");
        if (ct.getStatus() != null && ct.getStatus() != 1) throw new RuntimeException("该卡类型已禁用");
        Member m = memberMapper.selectById(dto.getMemberId());
        if (m == null) throw new RuntimeException("会员不存在");

        MemberCard card = new MemberCard();
        card.setCardNo("MC" + System.currentTimeMillis() + String.format("%04d", new Random().nextInt(10000)));
        card.setMemberId(m.getId());
        card.setMemberName(m.getName());
        card.setCardTypeId(ct.getId());
        card.setCardTypeName(ct.getTypeName());
        card.setCardKind(ct.getCardKind() != null ? ct.getCardKind() : 1);
        card.setStatus(1);
        if (ct.getCardKind() != null && ct.getCardKind() == 2) {
            card.setTotalTimes(ct.getTotalTimes() != null ? ct.getTotalTimes() : 0);
            card.setRemainTimes(card.getTotalTimes());
            card.setBalance(BigDecimal.ZERO);
        } else {
            card.setBalance(ct.getFaceValue() != null ? ct.getFaceValue() : BigDecimal.ZERO);
            card.setTotalTimes(0);
            card.setRemainTimes(0);
        }
        int validDays = ct.getValidDays() != null ? ct.getValidDays() : 365;
        card.setExpireTime(LocalDateTime.now().plusDays(validDays));
        memberCardMapper.insert(card);

        CardTransaction tx = new CardTransaction();
        tx.setCardId(card.getId());
        tx.setMemberId(m.getId());
        tx.setMemberName(m.getName());
        tx.setTransType("recharge");
        tx.setAmount(dto.getPayAmount() != null ? dto.getPayAmount() : ct.getPrice());
        tx.setBalanceBefore(BigDecimal.ZERO);
        tx.setBalanceAfter(card.getBalance());
        tx.setTimesBefore(0);
        tx.setTimesChange(card.getRemainTimes());
        tx.setTimesAfter(card.getRemainTimes());
        tx.setRemark(dto.getRemark());
        cardTransactionMapper.insert(tx);
        return card;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recharge(CardRechargeDTO dto) {
        MemberCard card = memberCardMapper.selectById(dto.getCardId());
        if (card == null) throw new RuntimeException("会员卡不存在");
        if (card.getCardKind() != null && card.getCardKind() == 2) throw new RuntimeException("次卡不支持充值，请购新卡");
        BigDecimal amt = dto.getAmount() != null ? dto.getAmount() : BigDecimal.ZERO;
        if (amt.compareTo(BigDecimal.ZERO) <= 0) throw new RuntimeException("充值金额须大于0");

        BigDecimal before = card.getBalance() != null ? card.getBalance() : BigDecimal.ZERO;
        BigDecimal after = before.add(amt);
        card.setBalance(after);
        memberCardMapper.updateById(card);

        CardTransaction tx = new CardTransaction();
        tx.setCardId(card.getId());
        tx.setMemberId(card.getMemberId());
        tx.setMemberName(card.getMemberName());
        tx.setTransType("recharge");
        tx.setAmount(amt);
        tx.setBalanceBefore(before);
        tx.setBalanceAfter(after);
        tx.setTimesBefore(card.getRemainTimes());
        tx.setTimesChange(0);
        tx.setTimesAfter(card.getRemainTimes());
        tx.setRemark(dto.getRemark());
        cardTransactionMapper.insert(tx);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> consume(CardConsumeDTO dto) {
        BigDecimal amount = dto.getAmount() != null ? dto.getAmount() : BigDecimal.ZERO;
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            Map<String, Object> r = new HashMap<>();
            r.put("deductedByTimes", 0);
            r.put("deductedByBalance", BigDecimal.ZERO);
            r.put("needCash", amount);
            return r;
        }
        LambdaQueryWrapper<MemberCard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberCard::getMemberId, dto.getMemberId()).eq(MemberCard::getStatus, 1);
        List<MemberCard> myCards = memberCardMapper.selectList(wrapper);
        List<MemberCard> cardsToDeduct = new ArrayList<>();
        for (MemberCard c : myCards) {
            MemberCard target = c.getMainCardId() != null ? memberCardMapper.selectById(c.getMainCardId()) : c;
            if (target != null && target.getStatus() != null && target.getStatus() == 1) cardsToDeduct.add(target);
        }
        Set<Long> added = new HashSet<>();
        List<MemberCard> list = new ArrayList<>();
        for (MemberCard c : cardsToDeduct) {
            if (added.add(c.getId())) list.add(c);
        }
        list.sort(Comparator
            .comparing(MemberCard::getCardKind, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(MemberCard::getExpireTime, Comparator.nullsLast(Comparator.naturalOrder())));

        int totalDeductedTimes = 0;
        BigDecimal totalDeductedBalance = BigDecimal.ZERO;
        Integer useTimes = dto.getUseTimes() != null && dto.getUseTimes() > 0 ? dto.getUseTimes() : null;
        String remark = dto.getRemark();
        String bizOrderNo = dto.getBizOrderNo();
        Member member = memberMapper.selectById(dto.getMemberId());
        String memberName = member != null ? member.getName() : "";

        for (MemberCard card : list) {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) break;
            if (card.getCardKind() != null && card.getCardKind() == 2) {
                int remain = card.getRemainTimes() != null ? card.getRemainTimes() : 0;
                if (remain <= 0) continue;
                int toUse = useTimes != null ? Math.min(useTimes - totalDeductedTimes, remain) : Math.min(1, remain);
                if (toUse <= 0) continue;
                CardType ct = cardTypeMapper.selectById(card.getCardTypeId());
                BigDecimal valuePerTime = (ct != null && ct.getFaceValue() != null) ? ct.getFaceValue() : BigDecimal.ZERO;
                BigDecimal deductAmount = valuePerTime.multiply(BigDecimal.valueOf(toUse));
                if (deductAmount.compareTo(amount) > 0) {
                    toUse = amount.divide(valuePerTime, 0, java.math.RoundingMode.DOWN).intValue();
                    if (toUse <= 0) continue;
                    deductAmount = valuePerTime.multiply(BigDecimal.valueOf(toUse));
                }
                int timesBefore = card.getRemainTimes();
                card.setRemainTimes(timesBefore - toUse);
                memberCardMapper.updateById(card);
                totalDeductedTimes += toUse;
                amount = amount.subtract(deductAmount);

                CardTransaction tx = new CardTransaction();
                tx.setCardId(card.getId());
                tx.setMemberId(dto.getMemberId());
                tx.setMemberName(memberName);
                tx.setTransType("consume");
                tx.setAmount(deductAmount.negate());
                tx.setTimesBefore(timesBefore);
                tx.setTimesChange(-toUse);
                tx.setTimesAfter(card.getRemainTimes());
                tx.setBalanceBefore(card.getBalance());
                tx.setBalanceAfter(card.getBalance());
                tx.setRemark(remark);
                tx.setBizOrderNo(bizOrderNo);
                cardTransactionMapper.insert(tx);
            } else {
                BigDecimal bal = card.getBalance() != null ? card.getBalance() : BigDecimal.ZERO;
                if (bal.compareTo(BigDecimal.ZERO) <= 0) continue;
                BigDecimal deduct = bal.min(amount);
                BigDecimal before = card.getBalance();
                card.setBalance(before.subtract(deduct));
                memberCardMapper.updateById(card);
                totalDeductedBalance = totalDeductedBalance.add(deduct);
                amount = amount.subtract(deduct);

                CardTransaction tx = new CardTransaction();
                tx.setCardId(card.getId());
                tx.setMemberId(dto.getMemberId());
                tx.setMemberName(memberName);
                tx.setTransType("consume");
                tx.setAmount(deduct.negate());
                tx.setBalanceBefore(before);
                tx.setBalanceAfter(card.getBalance());
                tx.setTimesBefore(card.getRemainTimes());
                tx.setTimesChange(0);
                tx.setTimesAfter(card.getRemainTimes());
                tx.setRemark(remark);
                tx.setBizOrderNo(bizOrderNo);
                cardTransactionMapper.insert(tx);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("deductedByTimes", totalDeductedTimes);
        result.put("deductedByBalance", totalDeductedBalance);
        result.put("needCash", amount);
        return result;
    }
}
