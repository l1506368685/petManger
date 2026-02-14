package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.SysDictItem;
import com.jsonll.base.entity.SysDictType;
import com.jsonll.base.request.DictItemPageRequest;

import java.util.List;

public interface SysDictService {
    List<SysDictType> typeList();
    void addType(SysDictType type);
    void updateType(SysDictType type);
    void deleteType(Long id);
    IPage<SysDictItem> itemPageList(DictItemPageRequest request);
    List<SysDictItem> itemsByType(String dictType);
    void addItem(SysDictItem item);
    void updateItem(SysDictItem item);
    void deleteItem(Long id);
    SysDictItem itemDetail(Long id);
}
