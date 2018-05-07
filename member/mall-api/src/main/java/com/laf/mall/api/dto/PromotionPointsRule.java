package com.laf.mall.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PromotionPointsRule {

    private int ppr_id;

    private Date start_date;

    private Date end_date;

    private BigDecimal amount;

    private String rule_name;

    private int mall_id;

    private Date birthday_start;

    private Date birthday_end;

    private String rule_content;

    public long getStart_date() {
        return start_date.getTime();
    }

    public long getEnd_date() {
        return end_date.getTime();
    }

    public long getBirthday_start() {
        return birthday_start.getTime();
    }

    public long getBirthday_end() {
        return birthday_end.getTime();
    }
}
