package ru.nbdev.instagrammclient.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "table_pixabay_photos_list")
public class Photo {

    @PrimaryKey
    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("webformatURL")
    public String webformatURL;

    @Expose
    @SerializedName("largeImageURL")
    public String largeImageURL;

    @Expose
    @SerializedName("previewURL")
    public String previewURL;

    @Expose
    @SerializedName("likes")
    public int likes;
}
