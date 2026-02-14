package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.MedicalRecord;
import com.jsonll.base.mapper.MedicalRecordMapper;
import com.jsonll.base.request.MedicalPageRequest;
import com.jsonll.base.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    @Override
    public IPage<MedicalRecord> pageList(MedicalPageRequest request) {
        Page<MedicalRecord> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<MedicalRecord> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getPetName())) wrapper.like(MedicalRecord::getPetName, request.getPetName());
        if (StringUtils.hasText(request.getDoctor())) wrapper.like(MedicalRecord::getDoctor, request.getDoctor());
        if (StringUtils.hasText(request.getSymptom())) wrapper.like(MedicalRecord::getSymptom, request.getSymptom());
        if (request.getStartDate() != null) wrapper.ge(MedicalRecord::getVisitDate, request.getStartDate());
        if (request.getEndDate() != null) wrapper.le(MedicalRecord::getVisitDate, request.getEndDate());
        wrapper.orderByDesc(MedicalRecord::getVisitDate);
        return medicalRecordMapper.selectPage(page, wrapper);
    }

    @Override
    public void add(MedicalRecord record) {
        medicalRecordMapper.insert(record);
    }

    @Override
    public void update(MedicalRecord record) {
        medicalRecordMapper.updateById(record);
    }

    @Override
    public void delete(Long id) {
        medicalRecordMapper.deleteById(id);
    }

    @Override
    public MedicalRecord detail(Long id) {
        return medicalRecordMapper.selectById(id);
    }
}
