package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.Member;
import com.jsonll.base.request.MemberPageRequest;
import com.jsonll.base.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/pageList")
    public R<?> pageList(MemberPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(memberService.pageList(request));
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody Member member) {
        memberService.add(member);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody Member member) {
        memberService.update(member);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        memberService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<Member> detail(@RequestParam("id") Long id) {
        return R.ok(memberService.detail(id));
    }
}
