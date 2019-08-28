package pl.pawelec.webshop.service.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static String getCurrentLocalDateTime() {
        return now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

}
