package com.laf.mall.api.service;

import com.laf.mall.api.dto.Industry;
import com.laf.mall.api.dto.PlaneMap;
import com.laf.mall.api.dto.Shop;
import com.laf.mall.api.querycondition.ShopQueryCondition;
import com.laf.mall.api.querycondition.points.PointsActionCondition;
import com.laf.mall.api.querycondition.points.PointsQueryCondition;
import com.laf.mall.api.querycondition.points.PointsSaveCondition;

import java.util.List;

public interface ShopService {

    /**
     * 获取商户详情
     *
     * @param shopId
     * @return 商户信息
     */
    Shop getShopDetail(final Integer shopId);

    /**
     * 获取商户详情
     *
     * @param shopname
     * @return 商户信息
     */
    Shop getShopDetail(final String shopname);

    /**
     * 获取商户列表
     *
     * @param condition
     * @return 商户列表
     */
    List<Shop> getShopList(final ShopQueryCondition condition);

    /**
     * 获得所有业态
     * @param mallId
     * @return 业态列表
     */
    List<Industry> getIndustries(final Integer mallId);

    /**
     * 获得所有平面地图
     * @param mallId
     * @return 地图列表
     */
    List<PlaneMap> getPlaneMap(final Integer mallId);
}
