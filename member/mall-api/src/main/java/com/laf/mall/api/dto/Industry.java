package com.laf.mall.api.dto;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Industry {
    public interface IndustryAppView {};

    /**
     * 业态id
     */
    @JsonView(IndustryAppView.class)
    private int industry_id;

    /**
     * 业态名称
     */
    @JsonView(IndustryAppView.class)
    private String industry_name;

    /**
     * 相关商城id
     */
    private int mall_id;
}
