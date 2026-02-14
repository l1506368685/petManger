package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.Pet;
import com.jsonll.base.request.PetPageRequest;

import java.util.List;

public interface PetService {
    IPage<Pet> pageList(PetPageRequest request);
    void add(Pet pet);
    void update(Pet pet);
    void delete(Long id);
    Pet detail(Long id);
    List<Pet> listAll();
}
