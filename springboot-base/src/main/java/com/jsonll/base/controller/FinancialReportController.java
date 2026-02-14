package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.service.FinancialReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * 多维度财务报表
 */
@RestController
@RequestMapping("/report")
public class FinancialReportController {

    @Autowired
    private FinancialReportService financialReportService;

    /** 日报：date 格式 yyyy-MM-dd */
    @GetMapping("/daily")
    public R<Map<String, Object>> daily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return R.ok(financialReportService.dailyReport(date));
    }

    /** 月报 */
    @GetMapping("/monthly")
    public R<Map<String, Object>> monthly(
            @RequestParam int year,
            @RequestParam int month) {
        return R.ok(financialReportService.monthlyReport(year, month));
    }

    /** 季报：quarter 1-4 */
    @GetMapping("/quarterly")
    public R<Map<String, Object>> quarterly(
            @RequestParam int year,
            @RequestParam int quarter) {
        return R.ok(financialReportService.quarterlyReport(year, quarter));
    }

    /** 年报 */
    @GetMapping("/yearly")
    public R<Map<String, Object>> yearly(@RequestParam int year) {
        return R.ok(financialReportService.yearlyReport(year));
    }

    /** 自定义报表；可选 compareStart/compareEnd 做对比 */
    @GetMapping("/custom")
    public R<Map<String, Object>> custom(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate compareStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate compareEnd) {
        return R.ok(financialReportService.customReport(startDate, endDate, compareStart, compareEnd));
    }
}
