package com.laf.mall.api.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberVisitLog {

    private int mvl_id;

    private int member_id;

    private int mall_id;

    private String open_id;

    private Date visit_time;

    private Date simple_time;

    public long getVisit_time() {
        return visit_time == null ? 0L : visit_time.getTime();
    }

    public long getSimple_time() {
        return simple_time == null ? 0L : simple_time.getTime();
    }
}
