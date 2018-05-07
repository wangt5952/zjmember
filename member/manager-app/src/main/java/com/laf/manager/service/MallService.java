package com.laf.manager.service;

import com.laf.manager.dto.Industry;
import com.laf.manager.dto.Mall;
import com.laf.manager.dto.Member;
import com.laf.manager.dto.PlaneMap;
import com.laf.manager.querycondition.mall.MallEditCondition;
import com.laf.manager.querycondition.member.MemberFilterCondition;
import com.laf.manager.querycondition.member.MemberListQueryCondition;

import java.math.BigDecimal;
import java.util.List;

public interface MallService {

    int editMall(MallEditCondition condition);

    Mall getMallByAppId(final String appId);

    List<Industry> getIndustries();

    int editIndustry(final Industry industry);

    Industry getIndustry(final Integer industryId);

    int deleteIndustry(final Integer industryId);

    List<PlaneMap> getPlaneMapList();

    PlaneMap getPlaneMapById(final Integer mapId);

    int editPlaneMap(PlaneMap map);

    int deletePlaneMap(final Integer mapId);

    List<Member> getMembers(MemberFilterCondition condition);

    int getMembersCount(final MemberFilterCondition condition);

    Member getMemberById(final Integer memberId);

    BigDecimal getCumulateAmountSum(final MemberFilterCondition condition);
}
