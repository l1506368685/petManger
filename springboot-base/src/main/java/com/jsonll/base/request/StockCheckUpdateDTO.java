package com.jsonll.base.request;

import com.jsonll.base.entity.StockCheckItem;
import com.jsonll.base.entity.StockCheckMain;
import lombok.Data;

import java.util.List;

@Data
public class StockCheckUpdateDTO {
    private StockCheckMain main;
    private List<StockCheckItem> items;
}
