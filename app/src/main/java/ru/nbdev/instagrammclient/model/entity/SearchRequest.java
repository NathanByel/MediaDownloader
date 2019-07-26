package ru.nbdev.instagrammclient.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "table_photo_search_cache")
public class SearchRequest {

    public SearchRequest(@NonNull String request, Date date, Photo[] photos) {
        this.request = request;
        this.date = date;
        this.photos = photos;
    }

    @PrimaryKey
    @NonNull
    public String request;

    public Date date;

    public Photo[] photos;
}
