package com.laf.manager.dto;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Industry {

    /**
     * 业态id
     */
    private int industry_id;

    /**
     * 业态名称
     */
    private String industry_name;

    /**
     * 相关商城id
     */
    private int mall_id;

    private int sort_id;

    private List<Shop> shops;

    public Industry(int industry_id, String industry_name) {
        this.industry_id = industry_id;
        this.industry_name = industry_name;
    }
}
