package com.laf.mall.api.utils.datetime;

import org.springframework.stereotype.Component;
import sun.applet.Main;

import java.time.*;
import java.util.Date;

@Component
public class DateTimeUtils {

    public long getMilliWithoutTime(long milli) {
        LocalDate date = Instant.ofEpochMilli(milli).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime time = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0, 0);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli();
    }

    public LocalDateTime getDateTimeByMilli(long milli) {
        LocalDateTime time = Instant.ofEpochMilli(milli).atZone(ZoneId.systemDefault()).toLocalDateTime();

        return time;
    }

    public long getMilliByDateTime(int year, int month, int day, int hour, int minute, int second, int na) {
        LocalDateTime time = LocalDateTime.of(year, month, day, hour, minute, second, na);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli();
    }

    public long getMilliByDate(int year, int month, int day) {
        LocalDateTime time = LocalDateTime.of(year, month, day, 0, 0, 0, 0);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli();
    }

    public long getMilliWithoutTime(LocalDate date) {
        LocalDateTime time = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0, 0, 0, 0);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli();
    }

    public long getMilliWithoutTime(Date date) {
        LocalDate localDate = Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime time = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0, 0);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli();
    }

    public long getMilliByToDay() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime time = LocalDateTime.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth(), 0, 0, 0, 0);
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());

        return zdt.toInstant().toEpochMilli();
    }

    public static void main(String[] args)
    {
        DateTimeUtils util = new DateTimeUtils();


    }
}
