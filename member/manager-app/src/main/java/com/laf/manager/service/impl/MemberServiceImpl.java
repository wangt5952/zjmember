package com.laf.manager.service.impl;

import com.laf.manager.SettingsProperties;
import com.laf.manager.core.support.tuple.Tuple2;
import com.laf.manager.dao.MemberDao;
import com.laf.manager.dto.Member;
import com.laf.manager.enums.Degree;
import com.laf.manager.enums.Interest;
import com.laf.manager.enums.MemberStatus;
import com.laf.manager.enums.Occupation;
import com.laf.manager.service.MallService;
import com.laf.manager.service.MemberService;
import com.laf.manager.utils.datetime.DateTimeUtils;
import com.laf.manager.utils.poi.Generator;
import com.laf.manager.utils.poi.POICellStyle;
import com.sun.javafx.geom.transform.BaseTransform;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private SettingsProperties settingsProperties;

    @Autowired
    private Generator generator;

    @Autowired
    private MallService mallService;

    @Autowired
    private DateTimeUtils dateTimeUtils;

    @Override
    public List<Integer> getVisitTimesMonthly() {
        return memberDao.getVisitTimesMonthly(settingsProperties.getMallId());
    }

    @Override
    public List<Integer> getVisitCountMonthly() {
        return memberDao.getVisitCountMonthly(settingsProperties.getMallId());
    }

    @Override
    public List<Integer> getActiveMembersCountMonthly() {
        return memberDao.getActiveMembersCountMonthly(settingsProperties.getMallId());
    }

    @Override
    public int getMemberCountBySex(int sex) {
        return memberDao.getMemberCountBySex(sex);
    }

    @Override
    public int getMemberCountByAge(int min, int max) {
        return memberDao.getMemberCountByAge(min, max);
    }

    @Override
    public List<Integer> getMemberCountByBirthday() {
        return memberDao.getMemberCountByBirthday();
    }

    @Override
    public List<Integer> getRegisterCountMonthly() {
        return memberDao.getRegisterCountMonthly(settingsProperties.getMallId());
    }

    @Override
    public List<Integer> getMemberCountMonthly(String year) {
        int baseCount = memberDao.getMemberCountByYear(year, settingsProperties.getMallId());

        List<Integer> registerCount = memberDao.getRegisterCountMonthly(year, settingsProperties.getMallId());

        List<Integer> membersCount = new ArrayList<>();

        for (int count : registerCount) {
            baseCount += count;

            membersCount.add(baseCount);
        }

        return membersCount;
    }

    @Override
    public void print2Excel(List<Member> members, OutputStream out) {
        List<String> titles = Arrays.asList("名称","手机号","等级","性别", "生日", "职业", "学历", "兴趣", "可用积分","累计积分","累计消费","注册日期","状态");
//        List<Tuple2<List<String>, CellStyle>> data = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();

        members.stream().forEach(member-> {
            List<String> rowData = new ArrayList<>();
            rowData.add(member.getName());
            rowData.add(member.getMobile());
            rowData.add(member.getLevel());
            rowData.add(member.getSex_name());
            rowData.add(dateTimeUtils.getDataTimeString(member.getBirthday()));
            rowData.add(Occupation.valueOf(member.getOccupation()).occuName());
            rowData.add(Degree.valueOf(member.getDegree_of_education()).theName());
            rowData.add(member.getInterest());
            rowData.add(String.valueOf(member.getUsable_points()));
            rowData.add(String.valueOf(member.getCumulate_points()));
            rowData.add(String.valueOf(member.getCumulate_amount()));
            rowData.add(dateTimeUtils.getDataTimeString(member.getRegist_date()));
            rowData.add(MemberStatus.valueOf(member.getStatus()).theName());
//            Tuple2<List<String>, CellStyle> tuple2 = new Tuple2<>(rowData, POICellStyle.CELL_NORMAL_CENTER);

            data.add(rowData);
        });

        generator.generate(out, titles, data, "会员信息");
    }
}
