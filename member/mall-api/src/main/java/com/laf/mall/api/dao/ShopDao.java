package com.laf.mall.api.dao;

import com.laf.mall.api.dto.Shop;
import com.laf.mall.api.querycondition.ShopQueryCondition;
import com.laf.mall.api.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShopDao {

    @Autowired
    ShopRepository repository;

    public Shop getShopDetail(final Integer shopId) {
        return repository.selectShopById(shopId);
    }

    public Shop getShopDetail(final String shopname) {
        return repository.selectShopByname(shopname);
    }

    public List<Shop> getShopList(final ShopQueryCondition condition) {
        return repository.selectShopList(condition);
    }

    public List<Shop> getAllShopList(final Integer mallId) {
        return repository.selectAllShopsByMall(mallId);
    }

    public List<Shop> getShopListByIndustry(final Integer industryId, final Integer mallId) {
        return repository.selectShopListByIndustry(industryId, mallId);
    }
}
