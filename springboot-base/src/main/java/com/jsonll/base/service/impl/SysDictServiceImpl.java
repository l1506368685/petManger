package com.jsonll.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsonll.base.entity.SysDictItem;
import com.jsonll.base.entity.SysDictType;
import com.jsonll.base.mapper.SysDictItemMapper;
import com.jsonll.base.mapper.SysDictTypeMapper;
import com.jsonll.base.request.DictItemPageRequest;
import com.jsonll.base.service.SysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictTypeMapper dictTypeMapper;
    @Autowired
    private SysDictItemMapper dictItemMapper;

    @Override
    public List<SysDictType> typeList() {
        return dictTypeMapper.selectList(new LambdaQueryWrapper<SysDictType>().orderByAsc(SysDictType::getSort));
    }

    @Override
    public void addType(SysDictType type) {
        dictTypeMapper.insert(type);
    }

    @Override
    public void updateType(SysDictType type) {
        dictTypeMapper.updateById(type);
    }

    @Override
    public void deleteType(Long id) {
        SysDictType t = dictTypeMapper.selectById(id);
        if (t != null) dictItemMapper.delete(new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getDictType, t.getDictType()));
        dictTypeMapper.deleteById(id);
    }

    @Override
    public IPage<SysDictItem> itemPageList(DictItemPageRequest request) {
        Page<SysDictItem> page = new Page<>(request.getCurrent(), request.getSize());
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(request.getDictType())) wrapper.eq(SysDictItem::getDictType, request.getDictType());
        if (StringUtils.hasText(request.getItemLabel())) wrapper.like(SysDictItem::getItemLabel, request.getItemLabel());
        wrapper.orderByAsc(SysDictItem::getSort);
        return dictItemMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SysDictItem> itemsByType(String dictType) {
        return dictItemMapper.selectList(
                new LambdaQueryWrapper<SysDictItem>()
                        .eq(SysDictItem::getDictType, dictType)
                        .eq(SysDictItem::getStatus, 1)
                        .orderByAsc(SysDictItem::getSort));
    }

    @Override
    public void addItem(SysDictItem item) {
        dictItemMapper.insert(item);
    }

    @Override
    public void updateItem(SysDictItem item) {
        dictItemMapper.updateById(item);
    }

    @Override
    public void deleteItem(Long id) {
        dictItemMapper.deleteById(id);
    }

    @Override
    public SysDictItem itemDetail(Long id) {
        return dictItemMapper.selectById(id);
    }
}
