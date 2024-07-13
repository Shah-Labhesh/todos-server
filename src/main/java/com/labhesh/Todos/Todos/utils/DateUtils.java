package com.labhesh.Todos.Todos.utils;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static LocalDateTime convertStringToTimestamp(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public static ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Kathmandu"));
    }
}
