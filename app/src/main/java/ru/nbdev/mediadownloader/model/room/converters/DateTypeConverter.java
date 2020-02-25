package ru.nbdev.mediadownloader.model.room.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateTypeConverter {

    @TypeConverter
    public static Date fromLong(long date) {
        return new Date(date);
    }

    @TypeConverter
    public static long fromDate(Date date) {
        return date.getTime();
    }
}
