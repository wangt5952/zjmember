package com.laf.manager.querycondition.shop;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ShopQueryCondition {

    /**
     * 商城id
     */
    private Integer mall_id;

    /**
     * 搜索关键字
     */
    private String keywords;

    /**
     * 楼层
     */
    private Integer map_id;

    /**
     * 业态id
     */
    private Integer industry_id;

    /**
     * 分页
     */
    private Integer page = 1;
    private Integer size = 10;
//    private int offset;

    public int getOffset() {
        return (page - 1) * size;
    }

    public Integer getMall_id() {
        return mall_id == null ? 0 : mall_id;
    }

    public Integer getMap_id() {
        return map_id == null ? 0 : map_id;
    }

    public Integer getIndustry_id() {
        return industry_id == null ? 0 : industry_id;
    }

    public Integer getPage() {
        return page == null ? 0 : page;
    }

    public Integer getSize() {
        return size == null ? 0 : size;
    }
}
