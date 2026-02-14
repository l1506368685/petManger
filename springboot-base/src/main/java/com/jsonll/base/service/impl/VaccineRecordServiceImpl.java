package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.VaccineRecord;
import com.jsonll.base.mapper.VaccineRecordMapper;
import com.jsonll.base.request.VaccinePageRequest;
import com.jsonll.base.service.VaccineRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class VaccineRecordServiceImpl implements VaccineRecordService {

    @Autowired
    private VaccineRecordMapper vaccineRecordMapper;

    @Override
    public IPage<VaccineRecord> pageList(VaccinePageRequest request) {
        Page<VaccineRecord> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<VaccineRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getVaccineName())) wrapper.like(VaccineRecord::getVaccineName, request.getVaccineName());
        if (StringUtils.hasText(request.getPetName())) wrapper.like(VaccineRecord::getPetName, request.getPetName());
        if (StringUtils.hasText(request.getDoctor())) wrapper.like(VaccineRecord::getDoctor, request.getDoctor());
        if (request.getStartDate() != null) wrapper.ge(VaccineRecord::getVaccineDate, request.getStartDate());
        if (request.getEndDate() != null) wrapper.le(VaccineRecord::getVaccineDate, request.getEndDate());
        wrapper.orderByDesc(VaccineRecord::getVaccineDate);
        return vaccineRecordMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(VaccineRecord record) {
        vaccineRecordMapper.insert(record);
    }

    @Override
    public void update(VaccineRecord record) {
        vaccineRecordMapper.updateById(record);
    }

    @Override
    public void delete(Long id) {
        vaccineRecordMapper.deleteById(id);
    }

    @Override
    public VaccineRecord detail(Long id) {
        return vaccineRecordMapper.selectById(id);
    }
}
