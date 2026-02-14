package com.jsonll.base.request;

import lombok.Data;

@Data
public class PageRequest {
    private Long current = 1L;
    private Long size = 10L;
}
