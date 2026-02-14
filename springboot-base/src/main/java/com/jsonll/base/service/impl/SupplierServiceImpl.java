package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.Supplier;
import com.jsonll.base.mapper.SupplierMapper;
import com.jsonll.base.request.SupplierPageRequest;
import com.jsonll.base.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierMapper supplierMapper;

    @Override
    public IPage<Supplier> pageList(SupplierPageRequest request) {
        Page<Supplier> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getName())) wrapper.like(Supplier::getName, request.getName());
        if (request.getStatus() != null) wrapper.eq(Supplier::getStatus, request.getStatus());
        wrapper.orderByDesc(Supplier::getCreateTime);
        return supplierMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Supplier> listAll(Integer status) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        if (status != null) wrapper.eq(Supplier::getStatus, status);
        wrapper.orderByAsc(Supplier::getName);
        return supplierMapper.selectList(wrapper);
    }

    @Override
    public void add(Supplier supplier) {
        supplierMapper.insert(supplier);
    }

    @Override
    public void update(Supplier supplier) {
        supplierMapper.updateById(supplier);
    }

    @Override
    public void delete(Long id) {
        supplierMapper.deleteById(id);
    }

    @Override
    public Supplier detail(Long id) {
        return supplierMapper.selectById(id);
    }
}
