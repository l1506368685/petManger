package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private PetMapper petMapper;
    @Autowired
    private OrderMainMapper orderMainMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;

    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        Map<String, Object> map = new HashMap<>();
        map.put("petCount", petMapper.selectCount(null));
        map.put("orderCount", orderMainMapper.selectCount(null));
        map.put("memberCount", memberMapper.selectCount(null));
        BigDecimal totalIncome = rechargeRecordMapper.selectList(null).stream()
                .map(r -> r.getPayAmount() != null ? r.getPayAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        map.put("totalIncome", totalIncome);
        return R.ok(map);
    }
}
