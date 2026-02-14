package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.PetType;
import com.jsonll.base.request.PetTypePageRequest;
import com.jsonll.base.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/petType")
public class PetTypeController {

    @Autowired
    private PetTypeService petTypeService;

    @GetMapping("/pageList")
    public R<?> pageList(PetTypePageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(petTypeService.pageList(request));
    }

    @GetMapping("/listAll")
    public R<List<PetType>> listAll() {
        return R.ok(petTypeService.listAll());
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody PetType petType) {
        petTypeService.add(petType);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody PetType petType) {
        petTypeService.update(petType);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        petTypeService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<PetType> detail(@RequestParam("id") Long id) {
        return R.ok(petTypeService.detail(id));
    }
}
