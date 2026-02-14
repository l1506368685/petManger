package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.PetType;
import com.jsonll.base.request.PetTypePageRequest;

import java.util.List;

public interface PetTypeService {
    IPage<PetType> pageList(PetTypePageRequest request);
    void add(PetType petType);
    void update(PetType petType);
    void delete(Long id);
    PetType detail(Long id);
    List<PetType> listAll();
}
