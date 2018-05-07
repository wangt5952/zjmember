package com.laf.manager.querycondition.shop;

import lombok.Data;

@Data
public class ShopSaveCondition {

    private Integer sortId;

    private String shopName;

    private Integer industryId;

    private Integer mapId;

    private String brand;

    private String linkman;

    private String phone;

    private String mobile;

    private String businessHours;

    private String pictures;

    private String logo;

    private String intro;

    private Integer shopId;

    private String mapName;

    private String industryName;

    private String berthNumber;

    private String shopImages;

    public Integer getSortId() {
        return sortId == null ? 0 : sortId;
    }

    public Integer getIndustryId() {
        return industryId == null ? 0 : industryId;
    }

    public Integer getMapId() {
        return mapId == null ? 0 : mapId;
    }

    public Integer getShopId() {
        return shopId == null ? 0 : shopId;
    }
}
