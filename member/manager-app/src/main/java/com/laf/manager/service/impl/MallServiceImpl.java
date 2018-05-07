package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.dao.*;
import com.laf.manager.dto.Industry;
import com.laf.manager.dto.Mall;
import com.laf.manager.dto.Member;
import com.laf.manager.dto.PlaneMap;
import com.laf.manager.querycondition.mall.MallEditCondition;
import com.laf.manager.querycondition.member.MemberFilterCondition;
import com.laf.manager.querycondition.member.MemberListQueryCondition;
import com.laf.manager.service.MallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MallServiceImpl implements MallService {

    @Autowired
    MallDao mallDao;

    @Autowired
    ArticlesDao articlesDao;

    @Autowired
    IndustryDao industryDao;

    @Autowired
    PlaneMapDao planeMapDao;

    @Autowired
    MemberDao memberDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Override
    public int editMall(MallEditCondition condition) {
        Mall mall = mallDao.getMallById(condition.getMallId());

        if (mall == null) {
            return 0;
        }

        mall.setAddress(condition.getAddress());
        mall.setMall_id(condition.getMallId());
        mall.setAppId(condition.getAppid());
        mall.setAppSecret(condition.getAppsecret());
        mall.setBusiness_hours(condition.getBusinessHours());
        mall.setPictures(condition.getPictures());
        mall.setLongitude(condition.getLongitude());
        mall.setLatitude(condition.getLatitude());
        mall.setMall_phone(condition.getPhone());
        mall.setHome(condition.getHome());

        int result = 0;

        if (mall.getIntro_id() == 0) {
            result = articlesDao.saveArticle(condition.getIntro(), condition.getMallId());

            if (result > 0) {
                mall.setIntro_id(result);
            } else  {
                return 0;
            }
        } else {
            result = articlesDao.editArticle(mall.getIntro_id(), condition.getIntro());
        }

        if (result == 0) {
            return 0;
        }

        result = mallDao.editMall(mall);

        return result;
    }

    @Override
    public Mall getMallByAppId(String appId) {
        return mallDao.getMallByAppId(appId);
    }

    @Override
    public List<Industry> getIndustries() {
        return industryDao.getAllIndustries(settingsProperties.getMallId());
    }

    @Override
    public int editIndustry(Industry industry) {
        industry.setMall_id(settingsProperties.getMallId());
        Industry industryDetail = this.getIndustry(industry.getIndustry_id());

        int result = 0;

        if (industryDetail == null) {
            result = industryDao.saveIndustry(industry);
        } else {
            result = industryDao.editIndustry(industry);
        }

        return result;
    }

    @Override
    public Industry getIndustry(Integer industryId) {
        return industryDao.getIndustryById(industryId);
    }

    @Override
    public int deleteIndustry(Integer industryId) {
        return industryDao.deleteIndustryById(industryId);
    }

    @Override
    public List<PlaneMap> getPlaneMapList() {
        return planeMapDao.getAllPlaneMaps(settingsProperties.getMallId());
    }

    @Override
    public PlaneMap getPlaneMapById(final Integer mapId) {
        return planeMapDao.getPlaneMapById(mapId);
    }

    @Override
    public int editPlaneMap(PlaneMap map) {
        map.setMall_id(settingsProperties.getMallId());
        PlaneMap pm = planeMapDao.getPlaneMapById(map.getMap_id());

        int result = 0;

        if (pm != null) {
            if (StringUtils.isEmpty(map.getMap_picture())) {
                map.setMap_picture(pm.getMap_picture());
            }

            result = planeMapDao.updatePlaneMap(map);
        } else {
            result = planeMapDao.savePlaneMap(map);
        }

        return result;
    }

    @Override
    public int deletePlaneMap(Integer mapId) {
        return planeMapDao.deletePlaneMap(mapId);
    }

    @Override
    public List<Member> getMembers(MemberFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return memberDao.multipleQuery(condition);
    }

    @Override
    public int getMembersCount(final MemberFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return memberDao.multipleMembersCount(condition);
    }

    @Override
    public Member getMemberById(final Integer memberId) {
        return memberDao.getMemberById(memberId, settingsProperties.getMallId());
    }

    @Override
    public BigDecimal getCumulateAmountSum(final MemberFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return memberDao.getCumulateAmountSum(condition);
    }
}
