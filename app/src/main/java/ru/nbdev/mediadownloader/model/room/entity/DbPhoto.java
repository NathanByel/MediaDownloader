package ru.nbdev.mediadownloader.model.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos_table")
public class DbPhoto {

    @PrimaryKey
    @ColumnInfo(name = "_id")
    public long id;

    @ColumnInfo(name = "preview_url")
    public String previewURL;

    @ColumnInfo(name = "fullsize_url")
    public String fullSizeURL;

    @ColumnInfo(name = "likes")
    public int likes;

    @ColumnInfo(name = "views")
    public int views;


    public DbPhoto(long id, String previewURL, String fullSizeURL, int likes, int views) {
        this.id = id;
        this.previewURL = previewURL;
        this.fullSizeURL = fullSizeURL;
        this.likes = likes;
        this.views = views;
    }

    public long getId() {
        return id;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public String getFullSizeURL() {
        return fullSizeURL;
    }

    public int getLikes() {
        return likes;
    }

    public int getViews() {
        return views;
    }
}
