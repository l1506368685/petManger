package com.jsonll.base.mapper;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 多维度财务报表统计查询
 */
public interface FinancialReportMapper {

    /** 日报：按支付方式汇总订单收入（已付款/已完成） */
    List<Map<String, Object>> dailyOrderIncomeByPayMethod(@Param("date") LocalDate date);

    /** 日报：按支付方式汇总充值收入 */
    List<Map<String, Object>> dailyRechargeIncomeByPayMethod(@Param("date") LocalDate date);

    /** 日报：当日采购支出 */
    BigDecimal dailyPurchaseExpense(@Param("date") LocalDate date);

    /** 日报：当日索赔支出（按赔付时间或创建时间） */
    BigDecimal dailyClaimExpense(@Param("date") LocalDate date);

    /** 日报：当日订单笔数 */
    long dailyOrderCount(@Param("date") LocalDate date);

    /** 日报：当日充值笔数 */
    long dailyRechargeCount(@Param("date") LocalDate date);

    /** 月报：按科目汇总收入（商品/服务来自订单，充值为充值记录） */
    List<Map<String, Object>> monthlyIncomeBySubject(@Param("year") int year, @Param("month") int month);

    /** 月报：当月订单商品成本（order_item.cost * quantity） */
    BigDecimal monthlyOrderCost(@Param("year") int year, @Param("month") int month);

    /** 月报：当月采购支出 */
    BigDecimal monthlyPurchaseExpense(@Param("year") int year, @Param("month") int month);

    /** 月报：当月索赔支出 */
    BigDecimal monthlyClaimExpense(@Param("year") int year, @Param("month") int month);

    /** 上月同期收入（用于环比） */
    BigDecimal monthlyTotalIncome(@Param("year") int year, @Param("month") int month);

    /** 去年同月收入（用于同比） */
    BigDecimal monthlyTotalIncomeLastYear(@Param("year") int year, @Param("month") int month);

    /** 自定义时间段：收入 */
    BigDecimal customTotalIncome(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /** 自定义时间段：支出（采购+索赔） */
    BigDecimal customTotalExpense(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /** 自定义时间段：按日汇总收入（趋势） */
    List<Map<String, Object>> customIncomeTrendByDay(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /** 季报/年报：TOP10 商品销售额 */
    List<Map<String, Object>> topGoodsByAmount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("limit") int limit);
}
