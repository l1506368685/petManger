package com.jsonll.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jsonll.base.entity.OrderMain;
import com.jsonll.base.entity.MemberConsumeSum;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderMainMapper extends BaseMapper<OrderMain> {

    /** 近 N 个月内按会员汇总实付金额（仅统计已完成/已付款订单） */
    @Select("SELECT member_id AS memberId, COALESCE(SUM(total_amount),0) AS totalAmount FROM order_main " +
            "WHERE deleted=0 AND member_id IS NOT NULL AND order_time >= #{since} " +
            "AND status IN ('已完成','已付款') GROUP BY member_id")
    List<MemberConsumeSum> sumAmountByMemberSince(@Param("since") LocalDateTime since);
}
