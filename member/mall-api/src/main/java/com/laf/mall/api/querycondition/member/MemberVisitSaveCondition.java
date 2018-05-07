package com.laf.mall.api.querycondition.member;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class MemberVisitSaveCondition {

    @NotNull(message = "mallId不能为空")
    @Min(value = 1, message = "mallId无效")
    private Integer mallId;

    @NotNull(message = "memberId不能为空")
    @Min(value = 1, message = "memberId无效")
    private Integer memberId;

//    @NotNull(message = "openId不能为空")
    private String openId;

    public long getSimpleTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0, 0, 0);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli();
    }

    public long getCurr() {
        return new Date().getTime();
    }
}
