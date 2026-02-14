package com.jsonll.base.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * 多维度财务报表
 */
public interface FinancialReportService {

    /**
     * 日报：每日收入（按支付方式）、支出、毛利、交易笔数
     */
    Map<String, Object> dailyReport(LocalDate date);

    /**
     * 月报：收入构成（商品/充值）、成本、毛利率、同比/环比
     */
    Map<String, Object> monthlyReport(int year, int month);

    /**
     * 季报：本季累计、趋势、TOP10
     */
    Map<String, Object> quarterlyReport(int year, int quarter);

    /**
     * 年报：年度累计、趋势、TOP10
     */
    Map<String, Object> yearlyReport(int year);

    /**
     * 自定义报表：时间范围、对比周期、分类维度
     */
    Map<String, Object> customReport(LocalDate startDate, LocalDate endDate, LocalDate compareStart, LocalDate compareEnd);
}
