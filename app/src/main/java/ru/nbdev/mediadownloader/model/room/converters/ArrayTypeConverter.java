package ru.nbdev.mediadownloader.model.room.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ArrayTypeConverter {

    @TypeConverter
    public static int[] fromString(String value) {
        Type type = new TypeToken<int[]>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public static String fromArray(int[] array) {
        Gson gson = new Gson();
        return gson.toJson(array);
    }
}
