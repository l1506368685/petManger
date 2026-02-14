package com.jsonll.base.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsPageRequest extends PageRequest {
    private String goodsName;
    private String goodsCode;
    private String brand;
    private String category;
}
