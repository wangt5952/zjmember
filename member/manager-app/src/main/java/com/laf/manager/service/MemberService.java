package com.laf.manager.service;

import com.laf.manager.dto.Member;

import java.io.OutputStream;
import java.util.List;

public interface MemberService {

    List<Integer> getVisitTimesMonthly();

    List<Integer> getVisitCountMonthly();

    List<Integer> getActiveMembersCountMonthly();

    int getMemberCountBySex(int sex);

    int getMemberCountByAge(int min, int max);

    List<Integer> getMemberCountByBirthday();

    List<Integer> getRegisterCountMonthly();

    List<Integer> getMemberCountMonthly(String year);

    void print2Excel(List<Member> members, OutputStream out);
}
