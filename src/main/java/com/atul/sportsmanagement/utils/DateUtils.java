package com.atul.sportsmanagement.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtils {
    public static boolean thisWeek(Instant date){
        Instant noww = Instant.now();
        LocalDateTime now = LocalDateTime.now();
        long l = System.currentTimeMillis() + (long) (6 - (now.getDayOfWeek().getValue()%7)) * 24 * 60 * 60 * 1000;
        Instant end_of_week = new Date(l).toInstant();
        return date.isAfter(noww) && date.isBefore(end_of_week);

    }
    public static boolean thisMonth(Instant dateI){
        LocalDateTime date = LocalDateTime.ofInstant(dateI, ZoneOffset.UTC);
        LocalDateTime now = LocalDateTime.now();
        return date.getYear() == now.getYear() && date.getMonth() == now.getMonth() && date.getDayOfMonth() >= now.getDayOfMonth();
    }
    public static boolean nextMonth(Instant dateI){
        LocalDateTime date = LocalDateTime.ofInstant(dateI,ZoneOffset.UTC);
        LocalDateTime now = LocalDateTime.now();
        int nextMonth = now.getMonth().getValue()+1;
        int nextYear = now.getYear();
        if(nextMonth == 12){
            nextMonth = 0;
            nextYear++;
        }
        return date.getYear() == nextYear && date.getMonth().getValue() == nextMonth;
    }

}
