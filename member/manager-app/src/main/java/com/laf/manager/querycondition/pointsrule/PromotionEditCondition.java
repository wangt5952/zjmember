package com.laf.manager.querycondition.pointsrule;

import lombok.Data;

import java.math.BigDecimal;
import java.time.*;
import java.util.Date;

@Data
public class PromotionEditCondition {
    private Integer ruleId;

    private String ruleName;

    private Date startTime;

    private Date endTime;

    private BigDecimal amount;

    private String ruleContent;

    private Date birthdayStart;

    private Date birthdayEnd;

    public Integer getRuleId() {
        return ruleId == null ? 0 : ruleId;
    }

    public long getStartTime() {
        return startTime == null ? (new Date()).getTime() : startTime.getTime();
    }

    public long getEndTime() {
        return endTime == null ? (new Date()).getTime() : endTime.getTime();
    }

    public void setStartTime(long startTime) {
        this.startTime = new Date(startTime);
    }

    public void setEndTime(long endTime) {
        this.endTime = new Date(endTime);
    }

    public void setBirthdayStart(long birthdayStart) {
        if (birthdayStart > 0) {
            LocalDate localDate = Instant.ofEpochMilli(birthdayStart).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();

            LocalDateTime time =
                    LocalDateTime.of(now.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0, 0);

            ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());
            this.birthdayStart = new Date(zdt.toInstant().toEpochMilli());
        }
    }

    public void setBirthdayEnd(long birthdayEnd) {
        if (birthdayEnd > 0) {
            LocalDate localDate = Instant.ofEpochMilli(birthdayEnd).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();

            LocalDateTime time =
                    LocalDateTime.of(now.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0, 0);

            ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());
            this.birthdayEnd = new Date(zdt.toInstant().toEpochMilli());
        }
    }
}
