package com.laf.mall.api.service.impl;

import com.laf.mall.api.dao.IndustryDao;
import com.laf.mall.api.dao.PlaneMapDao;
import com.laf.mall.api.dao.ShopDao;
import com.laf.mall.api.dto.Industry;
import com.laf.mall.api.dto.PlaneMap;
import com.laf.mall.api.dto.Shop;
import com.laf.mall.api.querycondition.ShopQueryCondition;
import com.laf.mall.api.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Autowired
    ShopDao shopDao;

    @Autowired
    IndustryDao industryDao;

    @Autowired
    PlaneMapDao planeMapDao;


    @Override
    public Shop getShopDetail(Integer shopId) {
        return shopDao.getShopDetail(shopId);
    }

    @Override
    public Shop getShopDetail(String shopname) {
        return shopDao.getShopDetail(shopname);
    }

    @Override
    public List<Shop> getShopList(ShopQueryCondition condition) {
        return shopDao.getShopList(condition);
    }

    @Override
    public List<Industry> getIndustries(Integer mallId) {
        return industryDao.getAllIndustries(mallId);
    }

    @Override
    public List<PlaneMap> getPlaneMap(Integer mallId) {
        return planeMapDao.getAllPlaneMaps(mallId);
    }
}
