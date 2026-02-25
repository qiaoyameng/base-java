package com.washshop.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {

    private Long total;
    private Long pages;
    private Long current;
    private Long size;
    private List<T> records;

    public PageVO() {
    }

    public PageVO(Long total, Long current, Long size, List<T> records) {
        this.total = total;
        this.current = current;
        this.size = size;
        this.records = records;
        this.pages = (total + size - 1) / size;
    }
}
