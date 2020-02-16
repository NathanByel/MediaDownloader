package ru.nbdev.mediadownloader.model.room.converters;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class MapTypeConverter {

    @TypeConverter
    public static Map<String, Object> fromString(String value) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromMap(Map<String, Object> map) {
        return new Gson().toJson(map);
    }
}
