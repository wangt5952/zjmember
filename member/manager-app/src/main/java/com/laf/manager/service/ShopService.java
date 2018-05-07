package com.laf.manager.service;

import com.laf.manager.dto.Industry;
import com.laf.manager.dto.PlaneMap;
import com.laf.manager.dto.Shop;
import com.laf.manager.querycondition.shop.ShopQueryCondition;
import com.laf.manager.querycondition.shop.ShopSaveCondition;

import java.io.OutputStream;
import java.util.List;

public interface ShopService {

    List<Shop> getShopList(final ShopQueryCondition condition);

    Shop getShopById(final Integer shopId);

    int editShop(final ShopSaveCondition condition);

    int deleteShop(final Integer shopId);

    int getCountShops(final ShopQueryCondition condition);

    List<Industry> getShopsGroupByIndustry();

    List<Shop> getShopAllList();

    List<PlaneMap> getShopsGroupByMap();

    public void print2Excel(List<Shop> shops, OutputStream out);
}
