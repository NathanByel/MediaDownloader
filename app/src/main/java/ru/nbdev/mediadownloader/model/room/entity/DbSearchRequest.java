package ru.nbdev.mediadownloader.model.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

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

    public DbSearchRequest() {
    }

    @Ignore
    public DbSearchRequest(@NonNull String request) {
        this(request,null);
    }

    @Ignore
    public DbSearchRequest(@NonNull String request, Map<String, Object> extraData) {
        date = new Date();
        this.request = request;
        this.extraData = extraData;
    }

    public String getRequest() {
        return request;
    }

    public Date getDate() {
        return date;
    }

    public String getJsonExtraData() {
        return MapTypeConverter.fromMap(extraData);
    }
}
