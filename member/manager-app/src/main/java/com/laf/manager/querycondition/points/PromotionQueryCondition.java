package com.laf.manager.querycondition.points;

import com.laf.manager.utils.datetime.DateTimeUtils;
import lombok.Data;

import java.time.*;

@Data
public class PromotionQueryCondition {

    private Integer levelId;

    private Integer shopId;

    private Long birthday;

    private Long shoppingDate;

    public Long getShoppingDate() {
        return new DateTimeUtils().getMilliWithoutTime(shoppingDate);
    }

    public Long getToday() {
        LocalDate localDate = LocalDate.now();
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
