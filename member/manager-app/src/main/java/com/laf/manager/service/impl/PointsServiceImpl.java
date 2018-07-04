package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.core.exception.MallDBException;
import com.laf.manager.dao.MemberDao;
import com.laf.manager.dao.PointsDao;
import com.laf.manager.dto.Member;
import com.laf.manager.dto.Points;
import com.laf.manager.enums.Degree;
import com.laf.manager.enums.MemberStatus;
import com.laf.manager.enums.Occupation;
import com.laf.manager.enums.Sources;
import com.laf.manager.querycondition.points.PointsLogFilterCondition;
import com.laf.manager.querycondition.points.PointsManualCondition;
import com.laf.manager.service.PointsService;
import com.laf.manager.utils.datetime.DateTimeUtils;
import com.laf.manager.utils.poi.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class PointsServiceImpl implements PointsService {

    @Autowired
    PointsDao pointsDao;

    @Autowired
    SettingsProperties settingsProperties;

    @Autowired
    MemberDao memberDao;

    @Autowired
    DateTimeUtils dateTimeUtils;

    @Autowired
    Generator generator;

    @Override
    public List<Points> getPointsList(PointsLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        List<Points> list = pointsDao.multipleQuery(condition);
        return list;
    }

    @Override
    public int getLogsCount(PointsLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return pointsDao.multipleMembersCount(condition);
    }

    @Override
    public int manual(PointsManualCondition condition) throws MallDBException {
        int result = 0;

        condition.setSources(Sources.MANUAL.value());
        Points points = new Points();
        points.setPoints(condition.getPoints());
        points.setSources(condition.getSources());
        points.setHandle_date(condition.getCurrTime());
        points.setMember_id(condition.getMemberId());
        points.setMember_name(condition.getMemberName());
        points.setMember_mobile(condition.getMemberMobile());
        points.setMall_id(settingsProperties.getMallId());
        points.setDesc(condition.getDesc());

        result = pointsDao.savePoints(points);

        if (result == 0) return 0;

        Member member = memberDao.getMemberById(condition.getMemberId());

        if (member == null) {
            throw new MallDBException();
        }

        int newCumulatePoints = member.getCumulate_points() + condition.getPoints();
        int newUseablePoints = member.getUsable_points() + condition.getPoints();
        result = memberDao.updatePoints(condition.getMemberId(), newCumulatePoints, newUseablePoints);

        if (result == 0) {
            throw new MallDBException();
        }

        return result;
    }

    @Override
    public int getPointsSum(final PointsLogFilterCondition condition) {
        condition.setMallId(settingsProperties.getMallId());
        return pointsDao.selectPointsSum(condition);
    }

    @Override
    public void print2Excel(List<Points> points, OutputStream out) {
        List<String> titles = Arrays.asList("会员名称","会员手机号","消费金额","消费时间","店铺","业态" ,"消费单号", "积分", "来源", "操作人", "操作时间");
//        List<Tuple2<List<String>, CellStyle>> data = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        points.stream().forEach(point-> {
            List<String> rowData = new ArrayList<>();

            rowData.add(point.getMember_name());
            rowData.add(point.getMember_mobile());
            rowData.add(point.getAmount() == null ? "" : point.getAmount().toString());
            rowData.add(dateTimeUtils.getDataTimeString(point.getShopping_date()));
            rowData.add(point.getShop_name());
            rowData.add(point.getShop_industry());
            rowData.add(point.getTicket_no());
            rowData.add(String.valueOf(point.getPoints()));
            rowData.add(Sources.valueOf(point.getSources()).theName());
            rowData.add(point.getVc_name());
            rowData.add(dateTimeUtils.getDataTimeString(point.getHandle_date()));
//            Tuple2<List<String>, CellStyle> tuple2 = new Tuple2<>(rowData, POICellStyle.CELL_NORMAL_CENTER);

            data.add(rowData);
        });

        generator.generate(out, titles, data, "积分明细");
    }
}
