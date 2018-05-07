package com.laf.manager.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class PageVo {

    private int page;

    private int isCurr;

    public PageVo(int page) {
        this.page = page;
    }

    public static PageVo[] getPages(int total, int size, int curr) {

        PageVo[] pages = new PageVo[total % size == 0 ? total / size : total / size + 1];

        for (int i = 0; i < pages.length; i++) {
            pages[i] = new PageVo(i + 1);
        }
        pages[curr - 1].isCurr = 1;

        return pages;
    }

}
