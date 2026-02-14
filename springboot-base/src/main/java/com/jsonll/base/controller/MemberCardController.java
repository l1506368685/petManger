package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.MemberCard;
import com.jsonll.base.request.CardBuyDTO;
import com.jsonll.base.request.CardConsumeDTO;
import com.jsonll.base.request.CardRechargeDTO;
import com.jsonll.base.request.MemberCardPageRequest;
import com.jsonll.base.service.MemberCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/memberCard")
public class MemberCardController {

    @Autowired
    private MemberCardService memberCardService;

    @GetMapping("/pageList")
    public R<?> pageList(MemberCardPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(memberCardService.pageList(request));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody MemberCard memberCard) {
        memberCardService.add(memberCard);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody MemberCard memberCard) {
        memberCardService.update(memberCard);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        memberCardService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<MemberCard> detail(@RequestParam("id") Long id) {
        return R.ok(memberCardService.detail(id));
    }

    @PostMapping("/buy")
    public R<MemberCard> buy(@RequestBody CardBuyDTO dto) {
        return R.ok(memberCardService.buyCard(dto));
    }

    @PostMapping("/recharge")
    public R<Void> recharge(@RequestBody CardRechargeDTO dto) {
        memberCardService.recharge(dto);
        return R.ok();
    }

    @PostMapping("/consume")
    public R<Map<String, Object>> consume(@RequestBody CardConsumeDTO dto) {
        return R.ok(memberCardService.consume(dto));
    }
}
