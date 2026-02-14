package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.PetType;
import com.jsonll.base.mapper.PetTypeMapper;
import com.jsonll.base.request.PetTypePageRequest;
import com.jsonll.base.service.PetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PetTypeServiceImpl implements PetTypeService {

    @Autowired
    private PetTypeMapper petTypeMapper;

    @Override
    public IPage<PetType> pageList(PetTypePageRequest request) {
        Page<PetType> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<PetType> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getTypeName())) wrapper.like(PetType::getTypeName, request.getTypeName());
        wrapper.orderByAsc(PetType::getSort).orderByDesc(PetType::getCreateTime);
        return petTypeMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(PetType petType) {
        petTypeMapper.insert(petType);
    }

    @Override
    public void update(PetType petType) {
        petTypeMapper.updateById(petType);
    }

    @Override
    public void delete(Long id) {
        petTypeMapper.deleteById(id);
    }

    @Override
    public PetType detail(Long id) {
        return petTypeMapper.selectById(id);
    }

    @Override
    public List<PetType> listAll() {
        return petTypeMapper.selectList(new LambdaQueryWrapper<PetType>().eq(PetType::getStatus, 1).orderByAsc(PetType::getSort));
    }
}
