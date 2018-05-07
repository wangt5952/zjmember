package com.laf.manager.dao;

import com.laf.manager.dto.Shop;
import com.laf.manager.querycondition.shop.ShopQueryCondition;
import com.laf.manager.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShopDao {

    @Autowired
    ShopRepository repository;

    public Shop getShopById(final Integer shopId) {
        return repository.selectShopById(shopId);
    }

    public List<Shop> getShopList(final ShopQueryCondition condition) {
        return repository.selectShopList(condition);
    }

    public int getCountShops(final ShopQueryCondition condition) {
        return repository.selectCountShops(condition);
    }

    public int saveShop(Shop shop) {
        shop.setText1(shop.getShop_name());// TODO: 2017/11/12 text1=商户名称+首拼 
        return repository.insertShop(shop);
    }

    public int updateShop(Shop shop) {
        return repository.updateShop(shop);
    }

    public int deleteShop(final Integer shopId) {
        return repository.deleteShop(shopId);
    }

    public List<Shop> getAllShopList(final Integer mallId) {
        return repository.selectAllShopsByMall(mallId);
    }

    public List<Shop> getShopListByIndustry(final Integer industryId, final Integer mallId) {
        return repository.selectShopListByIndustry(industryId, mallId);
    }

    public List<Shop> getShopsByPlaneMap(final Integer mapId) {
        List<Shop> list;

        try {
            list = repository.selectShopsByPlantMap(mapId);
        } catch (EmptyResultDataAccessException e) {
            list = new ArrayList<>();
        }

        return list;
    }
}
