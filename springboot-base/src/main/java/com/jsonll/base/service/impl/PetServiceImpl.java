package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.Pet;
import com.jsonll.base.entity.PetType;
import com.jsonll.base.mapper.PetMapper;
import com.jsonll.base.mapper.PetTypeMapper;
import com.jsonll.base.request.PetPageRequest;
import com.jsonll.base.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetMapper petMapper;
    @Autowired
    private PetTypeMapper petTypeMapper;

    @Override
    public IPage<Pet> pageList(PetPageRequest request) {
        Page<Pet> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getPetName())) wrapper.like(Pet::getPetName, request.getPetName());
        if (StringUtils.hasText(request.getContactName())) wrapper.like(Pet::getContactName, request.getContactName());
        if (StringUtils.hasText(request.getContactPhone())) wrapper.like(Pet::getContactPhone, request.getContactPhone());
        wrapper.orderByDesc(Pet::getCreateTime);
        return petMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(Pet pet) {
        if (pet.getTypeId() != null) {
            PetType t = petTypeMapper.selectById(pet.getTypeId());
            if (t != null) pet.setTypeName(t.getTypeName());
        }
        petMapper.insert(pet);
    }

    @Override
    public void update(Pet pet) {
        if (pet.getTypeId() != null) {
            PetType t = petTypeMapper.selectById(pet.getTypeId());
            if (t != null) pet.setTypeName(t.getTypeName());
        }
        petMapper.updateById(pet);
    }

    @Override
    public void delete(Long id) {
        petMapper.deleteById(id);
    }

    @Override
    public Pet detail(Long id) {
        return petMapper.selectById(id);
    }

    @Override
    public List<Pet> listAll() {
        return petMapper.selectList(null);
    }
}
