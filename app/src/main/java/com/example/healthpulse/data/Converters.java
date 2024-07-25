package com.example.healthpulse.data;

import androidx.room.TypeConverter;
import org.threeten.bp.LocalDateTime;
import java.util.Date;

public class Converters {

    @TypeConverter
    public static LocalDateTime fromTimestamp(String value) {
        return value == null ? null : LocalDateTime.parse(value);
    }

    @TypeConverter
    public static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toString();
    }

    @TypeConverter
    public static Date fromLong(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }
}
