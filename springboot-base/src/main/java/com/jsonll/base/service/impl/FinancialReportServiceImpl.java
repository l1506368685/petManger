package com.jsonll.base.service.impl;

import com.jsonll.base.mapper.FinancialReportMapper;
import com.jsonll.base.service.FinancialReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FinancialReportServiceImpl implements FinancialReportService {

    @Autowired
    private FinancialReportMapper financialReportMapper;

    @Override
    public Map<String, Object> dailyReport(LocalDate date) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("date", date.toString());

        List<Map<String, Object>> orderByPay = financialReportMapper.dailyOrderIncomeByPayMethod(date);
        List<Map<String, Object>> rechargeByPay = financialReportMapper.dailyRechargeIncomeByPayMethod(date);
        Map<String, BigDecimal> incomeByPayMethod = new LinkedHashMap<>();
        for (Map<String, Object> row : orderByPay) {
            String pay = (String) row.get("payMethod");
            if (pay == null) pay = "现金";
            BigDecimal amt = toBigDecimal(row.get("amount"));
            incomeByPayMethod.merge(pay, amt, BigDecimal::add);
        }
        for (Map<String, Object> row : rechargeByPay) {
            String pay = (String) row.get("payMethod");
            if (pay == null) pay = "现金";
            BigDecimal amt = toBigDecimal(row.get("amount"));
            incomeByPayMethod.merge(pay, amt, BigDecimal::add);
        }
        result.put("incomeByPayMethod", incomeByPayMethod);
        BigDecimal totalIncome = incomeByPayMethod.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("totalIncome", totalIncome);

        BigDecimal purchaseExp = toBigDecimal(financialReportMapper.dailyPurchaseExpense(date));
        BigDecimal claimExp = toBigDecimal(financialReportMapper.dailyClaimExpense(date));
        BigDecimal totalExpense = purchaseExp.add(claimExp);
        result.put("purchaseExpense", purchaseExp);
        result.put("claimExpense", claimExp);
        result.put("totalExpense", totalExpense);
        result.put("grossProfit", totalIncome.subtract(totalExpense));

        long orderCount = financialReportMapper.dailyOrderCount(date);
        long rechargeCount = financialReportMapper.dailyRechargeCount(date);
        result.put("orderCount", orderCount);
        result.put("rechargeCount", rechargeCount);
        result.put("transactionCount", orderCount + rechargeCount);

        return result;
    }

    @Override
    public Map<String, Object> monthlyReport(int year, int month) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("year", year);
        result.put("month", month);

        List<Map<String, Object>> bySubject = financialReportMapper.monthlyIncomeBySubject(year, month);
        Map<String, BigDecimal> incomeBySubject = new LinkedHashMap<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        for (Map<String, Object> row : bySubject) {
            String sub = (String) row.get("subject");
            BigDecimal amt = toBigDecimal(row.get("amount"));
            incomeBySubject.put(sub, amt);
            totalIncome = totalIncome.add(amt);
        }
        result.put("incomeBySubject", incomeBySubject);
        result.put("totalIncome", totalIncome);

        BigDecimal orderCost = toBigDecimal(financialReportMapper.monthlyOrderCost(year, month));
        BigDecimal purchaseExp = toBigDecimal(financialReportMapper.monthlyPurchaseExpense(year, month));
        BigDecimal claimExp = toBigDecimal(financialReportMapper.monthlyClaimExpense(year, month));
        BigDecimal totalCost = orderCost.add(purchaseExp).add(claimExp);
        result.put("orderCost", orderCost);
        result.put("purchaseExpense", purchaseExp);
        result.put("claimExpense", claimExp);
        result.put("totalExpense", totalCost);
        result.put("grossProfit", totalIncome.subtract(totalCost));

        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            result.put("grossMarginRate", totalIncome.subtract(orderCost).divide(totalIncome, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
        } else {
            result.put("grossMarginRate", BigDecimal.ZERO);
        }

        int lastYear = month - 1 <= 0 ? year - 1 : year;
        int lastMonth = month - 1 <= 0 ? 12 : month - 1;
        BigDecimal lastMonthVal = toBigDecimal(financialReportMapper.monthlyTotalIncome(lastYear, lastMonth));
        BigDecimal lastYearSame = toBigDecimal(financialReportMapper.monthlyTotalIncomeLastYear(year, month));
        result.put("incomeLastMonth", lastMonthVal);
        result.put("incomeLastYearSameMonth", lastYearSame);
        result.put("chainRatio", lastMonthVal.compareTo(BigDecimal.ZERO) == 0 ? null : totalIncome.subtract(lastMonthVal).divide(lastMonthVal, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
        result.put("yearOnYearRatio", lastYearSame.compareTo(BigDecimal.ZERO) == 0 ? null : totalIncome.subtract(lastYearSame).divide(lastYearSame, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
        return result;
    }

    @Override
    public Map<String, Object> quarterlyReport(int year, int quarter) {
        int startMonth = (quarter - 1) * 3 + 1;
        LocalDate startDate = LocalDate.of(year, startMonth, 1);
        LocalDate endDate = startDate.plusMonths(3).minusDays(1);
        return periodReport(startDate, endDate, "季报", year + "年Q" + quarter);
    }

    @Override
    public Map<String, Object> yearlyReport(int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);
        return periodReport(startDate, endDate, "年报", year + "年");
    }

    private Map<String, Object> periodReport(LocalDate startDate, LocalDate endDate, String type, String label) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", type);
        result.put("label", label);
        result.put("startDate", startDate.toString());
        result.put("endDate", endDate.toString());

        BigDecimal totalIncome = toBigDecimal(financialReportMapper.customTotalIncome(startDate, endDate));
        BigDecimal totalExpense = toBigDecimal(financialReportMapper.customTotalExpense(startDate, endDate));
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("grossProfit", totalIncome.subtract(totalExpense));

        List<Map<String, Object>> trend = financialReportMapper.customIncomeTrendByDay(startDate, endDate);
        result.put("incomeTrendByDay", trend);

        List<Map<String, Object>> topGoods = financialReportMapper.topGoodsByAmount(startDate, endDate, 10);
        result.put("topGoods", topGoods);
        return result;
    }

    @Override
    public Map<String, Object> customReport(LocalDate startDate, LocalDate endDate, LocalDate compareStart, LocalDate compareEnd) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("startDate", startDate.toString());
        result.put("endDate", endDate.toString());

        BigDecimal totalIncome = toBigDecimal(financialReportMapper.customTotalIncome(startDate, endDate));
        BigDecimal totalExpense = toBigDecimal(financialReportMapper.customTotalExpense(startDate, endDate));
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("grossProfit", totalIncome.subtract(totalExpense));

        List<Map<String, Object>> trend = financialReportMapper.customIncomeTrendByDay(startDate, endDate);
        result.put("incomeTrendByDay", trend);

        if (compareStart != null && compareEnd != null) {
            BigDecimal compareIncome = toBigDecimal(financialReportMapper.customTotalIncome(compareStart, compareEnd));
            BigDecimal compareExpense = toBigDecimal(financialReportMapper.customTotalExpense(compareStart, compareEnd));
            result.put("compareStartDate", compareStart.toString());
            result.put("compareEndDate", compareEnd.toString());
            result.put("compareTotalIncome", compareIncome);
            result.put("compareTotalExpense", compareExpense);
            result.put("compareGrossProfit", compareIncome.subtract(compareExpense));
            if (compareIncome.compareTo(BigDecimal.ZERO) != 0) {
                result.put("incomeChangeRatio", totalIncome.subtract(compareIncome).divide(compareIncome, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
            }
        }
        return result;
    }

    private static BigDecimal toBigDecimal(Object o) {
        if (o == null) return BigDecimal.ZERO;
        if (o instanceof BigDecimal) return (BigDecimal) o;
        if (o instanceof Number) return BigDecimal.valueOf(((Number) o).doubleValue());
        try {
            return new BigDecimal(o.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
