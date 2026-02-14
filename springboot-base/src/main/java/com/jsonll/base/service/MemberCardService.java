package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.MemberCard;
import com.jsonll.base.request.CardBuyDTO;
import com.jsonll.base.request.CardConsumeDTO;
import com.jsonll.base.request.CardRechargeDTO;
import com.jsonll.base.request.MemberCardPageRequest;

import java.util.Map;

public interface MemberCardService {
    IPage<MemberCard> pageList(MemberCardPageRequest request);
    void add(MemberCard memberCard);
    void update(MemberCard memberCard);
    void delete(Long id);
    MemberCard detail(Long id);

    /** 购卡/开卡 */
    MemberCard buyCard(CardBuyDTO dto);

    /** 储值卡充值 */
    void recharge(CardRechargeDTO dto);

    /** 消费扣款：优先次卡次数再储值余额，返回扣款明细 */
    Map<String, Object> consume(CardConsumeDTO dto);
}
