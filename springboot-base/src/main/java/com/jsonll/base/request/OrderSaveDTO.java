package com.jsonll.base.request;

import com.jsonll.base.entity.OrderItem;
import com.jsonll.base.entity.OrderMain;
import lombok.Data;

import java.util.List;

@Data
public class OrderSaveDTO {
    private OrderMain order;
    private List<OrderItem> items;
}
