package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.MemberLevelRule;
import com.jsonll.base.request.MemberLevelRulePageRequest;
import com.jsonll.base.service.MemberLevelRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memberLevelRule")
public class MemberLevelRuleController {

    @Autowired
    private MemberLevelRuleService memberLevelRuleService;

    @GetMapping("/pageList")
    public R<?> pageList(MemberLevelRulePageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(memberLevelRuleService.pageList(request));
    }

    @GetMapping("/listAll")
    public R<List<MemberLevelRule>> listAll() {
        return R.ok(memberLevelRuleService.listAllEnabled());
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody MemberLevelRule entity) {
        memberLevelRuleService.add(entity);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody MemberLevelRule entity) {
        memberLevelRuleService.update(entity);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        memberLevelRuleService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<MemberLevelRule> detail(@RequestParam("id") Long id) {
        return R.ok(memberLevelRuleService.detail(id));
    }
}
