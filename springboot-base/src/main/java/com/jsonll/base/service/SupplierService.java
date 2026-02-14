package com.jsonll.base.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsonll.base.entity.Supplier;
import com.jsonll.base.request.SupplierPageRequest;

import java.util.List;

public interface SupplierService {
    IPage<Supplier> pageList(SupplierPageRequest request);
    List<Supplier> listAll(Integer status);
    void add(Supplier supplier);
    void update(Supplier supplier);
    void delete(Long id);
    Supplier detail(Long id);
}
