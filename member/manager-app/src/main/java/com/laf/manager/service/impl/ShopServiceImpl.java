package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.dao.ArticlesDao;
import com.laf.manager.dao.IndustryDao;
import com.laf.manager.dao.PlaneMapDao;
import com.laf.manager.dao.ShopDao;
import com.laf.manager.dto.Industry;
import com.laf.manager.dto.PlaneMap;
import com.laf.manager.dto.Shop;
import com.laf.manager.querycondition.shop.ShopQueryCondition;
import com.laf.manager.querycondition.shop.ShopSaveCondition;
import com.laf.manager.service.MallService;
import com.laf.manager.service.ShopService;
import com.laf.manager.utils.poi.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    ShopDao shopDao;

    @Autowired
    ArticlesDao articlesDao;

    @Autowired
    PlaneMapDao planeMapDao;

    @Autowired
    IndustryDao industryDao;

    @Autowired
    MallService mallService;

    @Autowired
    private Generator generator;

    @Autowired
    SettingsProperties settingsProperties;

    @Override
    public List<Shop> getShopList(ShopQueryCondition condition) {
        List<Shop> list = shopDao.getShopList(condition);

        return list;
    }

    @Override
    public Shop getShopById(Integer shopId) {
        Shop shop = shopDao.getShopById(shopId);

        return shop;
    }

    @Override
    public int editShop(ShopSaveCondition condition) {
        Shop shop = shopDao.getShopById(condition.getShopId());

        if (shop == null) {
            shop = new Shop();
        }

        PlaneMap map = planeMapDao.getPlaneMapById(condition.getMapId());
        Industry industry = industryDao.getIndustryById(condition.getIndustryId());

        if (map == null || industry == null) {
            return 0;
        }

        shop.setMall_id(settingsProperties.getMallId());
        shop.setShop_id(condition.getShopId());
        shop.setPlane_map(condition.getMapId());
        shop.setShop_name(condition.getShopName());
        shop.setIndustry(condition.getIndustryId());
//        shop.setIndustry_name(condition.getIndustryName());
        shop.setIndustry_name(industry.getIndustry_name());
//        shop.setMap_name(condition.getMapName());
        shop.setMap_name(map.getMap_name());
        shop.setPhone(condition.getPhone());
        shop.setBrand(condition.getBrand());
        shop.setBerth_number(condition.getBerthNumber());
        shop.setPhone(condition.getPhone());
        shop.setMobile(condition.getMobile());
        shop.setBusiness_hours(condition.getBusinessHours());
        shop.setIntro(condition.getIntro());
        shop.setSort(condition.getSortId());
        shop.setStatus(0);
        shop.setLinkman(condition.getLinkman());
        shop.setPictures(condition.getPictures());

        int result;
        if (condition.getShopId() == 0) { // insert

            result = articlesDao.saveArticle(shop.getIntro(), shop.getMall_id());

            if (result <= 0) return result;

            shop.setArticle_id(result);
            shop.setPictures(condition.getPictures());
            shop.setLogo(condition.getLogo());
            result = shopDao.saveShop(shop);

            if (result <= 0) {
                articlesDao.deleteArticle(shop.getArticle_id());
            }

        } else {
            articlesDao.editArticle(shop.getArticle_id(), shop.getIntro());

            if (!StringUtils.isEmpty(condition.getLogo())) {
                shop.setLogo(condition.getLogo());
            }

            result = shopDao.updateShop(shop);
        }

        return result;
    }

    @Override
    public int deleteShop(Integer shopId) {
        Shop shop = shopDao.getShopById(shopId);

        if (shop == null) return 0;

        int result = articlesDao.deleteArticle(shop.getArticle_id());

        if (result <= 0) return result;

        result = shopDao.deleteShop(shopId);

        return result;
    }

    @Override
    public int getCountShops(ShopQueryCondition condition) {
        return shopDao.getCountShops(condition);
    }

    @Override
    public List<Industry> getShopsGroupByIndustry() {
        List<Industry> industries = mallService.getIndustries();

        for (Industry industry : industries) {
            List<Shop> shops = shopDao.getShopListByIndustry(industry.getIndustry_id(), settingsProperties.getMallId());
            industry.setShops(shops);
        }

        return industries;
    }

    @Override
    public List<Shop> getShopAllList() {
        return shopDao.getAllShopList(settingsProperties.getMallId());
    }

    @Override
    public List<PlaneMap> getShopsGroupByMap() {
        List<PlaneMap> list = mallService.getPlaneMapList();
        list.stream()
                .forEach(planeMap -> planeMap.setShops(shopDao.getShopsByPlaneMap(planeMap.getMap_id())));

        return list;
    }

    @Override
    public void print2Excel(List<Shop> shops, OutputStream out) {
        List<String> titles = Arrays.asList("商户名称","业态","楼层","品牌名称", "位置", "联系人", "联系电话");
//        List<Tuple2<List<String>, CellStyle>> data = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        shops.stream().forEach(shop-> {
            List<String> rowData = new ArrayList<>();
            rowData.add(shop.getShop_name());
            rowData.add(shop.getIndustry_name());
            rowData.add(shop.getMap_name());
            rowData.add(shop.getBrand());
            rowData.add(shop.getBerth_number());
            rowData.add(shop.getLinkman());
            rowData.add(shop.getPhone());
//            Tuple2<List<String>, CellStyle> tuple2 = new Tuple2<>(rowData, POICellStyle.CELL_NORMAL_CENTER);

            data.add(rowData);
        });

        generator.generate(out, titles, data, "会员信息");
    }
}
