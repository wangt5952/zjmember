package com.laf.mall.api.querycondition.points;

import lombok.Data;

import java.time.*;
import java.util.Date;

@Data
public class PromotionQueryCondition {

    private Integer levelId;

    private Integer shopId;

    private Long birthday;

    private Long today;

    public Long getToday() {
        LocalDate localDate = Instant.ofEpochMilli(today).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime time = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0, 0);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli();
    }

    public Long getBirthday() {

        LocalDate localDate = LocalDate.now();

        LocalDate memberBirthday = Instant.ofEpochMilli(birthday).atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate today = LocalDate.of(localDate.getYear(), memberBirthday.getMonth(), memberBirthday.getDayOfMonth());

        LocalDateTime time = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0, 0, 0, 0);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli();
    }
}
