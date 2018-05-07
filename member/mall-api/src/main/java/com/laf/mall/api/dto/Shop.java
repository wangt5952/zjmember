package com.laf.mall.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Shop {
    public interface ShopAppListView {}
    public interface ShopAppView extends ShopAppListView {};
    public interface ShopAllView extends ShopAppView {};

    /**
     * 商户id
     */
    @JsonView(ShopAppListView.class)
    private int shop_id;

    /**
     * 商户名称
     */
    @JsonView(ShopAppListView.class)
    private String shop_name;

    /**
     * 平面地图存储地址
     */
    @JsonView(ShopAppView.class)
    private String map_picture;

    /**
     * 平面地图id
     */
    private int plane_map;

    /**
     * 商户介绍
     */
    @JsonView(ShopAppView.class)
    private String intro;

    /**
     * 联系电话
     */
    @JsonView(ShopAppView.class)
    private String phone;

    /**
     * 手机
     */
    @JsonView(ShopAppView.class)
    private String mobile;

    /**
     * 轮播图
     */
    @JsonView(ShopAppView.class)
    private String pictures;

    /**
     * 商户列表小图地址
     */
    @JsonView(ShopAppListView.class)
    private String logo;

    /**
     * 业态id
     */
    private int industry;

    /**
     * 业态名称
     */
    @JsonView(ShopAppListView.class)
    private String industry_name;

    /**
     * 商户门牌号
     */
    @JsonView(ShopAppListView.class)
    private String berth_number;

    /**
     * 积分热度
     */
    @JsonView(ShopAppListView.class)
    private int points;

    /**
     * 相关商城id
     */
    private int mall_id;

    /**
     * 排序号
     */
    private int sort;

    public Shop(int shop_id, String logo, String shop_name, String berth_number, String industry_name, int points) {
        this.shop_id = shop_id;
        this.logo = logo;
        this.shop_name = shop_name;
        this.berth_number = berth_number;
        this.industry_name = industry_name;
        this.points = points;
    }
}
