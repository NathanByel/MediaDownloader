package ru.nbdev.mediadownloader.model.room.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.Map;

import ru.nbdev.mediadownloader.model.room.converters.DateTypeConverter;
import ru.nbdev.mediadownloader.model.room.converters.MapTypeConverter;

@Entity(
        tableName = "search_requests_table",
        indices = {
                @Index(value = {"request", "extra_data"}, unique = true)
        }
)
public class DbSearchRequest {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public long id;

    @ColumnInfo(name = "request")
    public String request;

    @TypeConverters({DateTypeConverter.class})
    @ColumnInfo(name = "date")
    public Date date;

    @TypeConverters({MapTypeConverter.class})
    @ColumnInfo(name = "extra_data")
    public Map<String, Object> extraData;

    public DbSearchRequest(@NonNull String request, Map<String, Object> extraData) {
        date = new Date();
        this.request = request;
        this.extraData = extraData;
    }
}
