package com.laf.manager.dto;

import com.laf.manager.utils.Pinyin;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Shop {
//    public interface ShopAppListView {}
//    public interface ShopAppView extends ShopAppListView {};
//    public interface ShopAllView extends ShopAppView {};

    /**
     * 商户id
     */
    private int shop_id;

    /**
     * 商户名称
     */
    private String shop_name;

    /**
     * 平面地图名称
     */
    private String map_name;

    /**
     * 平面地图id
     */
    private int plane_map;

    /**
     * 商户介绍
     */
    private String intro;

    /**
     * 关联的内容介绍id
     */
    private int article_id;
    /**
     * 联系电话
     */
    private String phone;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 图组
     */
    private String pictures;

    /**
     * 商户
     */
    private String logo;

    /**
     * 业态id
     */
    private int industry;

    /**
     * 业态名称
     */
    private String industry_name;

    /**
     * 商户门牌号
     */
    private String berth_number;

    /**
     * 相关商城id
     */
    private int mall_id;

    /**
     * 排序号
     */
    private int sort;

    /**
     * 营业时间
     */
    private String business_hours;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 商铺状态
     */
    private int status;

    /**
     * 商铺状态名称
     */
    private String status_name;

    /**
     * 查询字段
     */
    private String text1;

    /**
     * 首拼
     */
    private String acronym;

    public Shop(int shop_id, String logo, String shop_name, String berth_number, String industry_name) {
        this.shop_id = shop_id;
        this.logo = logo;
        this.shop_name = shop_name;
        this.berth_number = berth_number;
        this.industry_name = industry_name;
    }

    public Shop(int shop_id, String shop_name, int mall_id) {
        this.shop_id = shop_id;
        this.shop_name = shop_name;
        this.mall_id = mall_id;
    }

    public String getStatus_name() {
        if (status == 0) status_name = "启用";
        else if (status == 1) status_name = "禁用";

        return status_name;
    }

    public String getAcronym() {
        return Pinyin.getPinYinHeadChar(this.shop_name);
    }

    public String getText1() {
        return this.shop_name + this.brand + this.industry_name + this.getAcronym();
    }
}
