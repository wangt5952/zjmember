package com.laf.manager.querycondition.member;

import lombok.Data;

@Data
public class MemberListQueryCondition {

    private int mallId;

    private int page = 1;

    private int size = 10;

    public int getOffset() {
        return (page - 1) * size;
    }
}
