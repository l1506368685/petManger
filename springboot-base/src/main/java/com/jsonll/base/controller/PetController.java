package com.jsonll.base.controller;

import com.jsonll.base.core.R;
import com.jsonll.base.entity.Pet;
import com.jsonll.base.request.PetPageRequest;
import com.jsonll.base.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping("/pageList")
    public R<?> pageList(PetPageRequest request) {
        if (request.getCurrent() == null) request.setCurrent(1L);
        if (request.getSize() == null) request.setSize(10L);
        return R.ok(petService.pageList(request));
    }

    @GetMapping("/listAll")
    public R<List<Pet>> listAll() {
        return R.ok(petService.listAll());
    }

    @PostMapping("/add")
    public R<Void> add(@RequestBody Pet pet) {
        petService.add(pet);
        return R.ok();
    }

    @PutMapping("/update")
    public R<Void> update(@RequestBody Pet pet) {
        petService.update(pet);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R<Void> delete(@RequestParam("id") Long id) {
        petService.delete(id);
        return R.ok();
    }

    @GetMapping("/detail")
    public R<Pet> detail(@RequestParam("id") Long id) {
        return R.ok(petService.detail(id));
    }
}
