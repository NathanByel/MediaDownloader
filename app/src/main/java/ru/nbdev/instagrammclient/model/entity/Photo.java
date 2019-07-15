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
    @SerializedName("type")
    public String type;

    @Expose
    @SerializedName("previewURL")
    public String previewURL;

    @Expose
    @SerializedName("webformatURL")
    public String webformatURL;

    @Expose
    @SerializedName("largeImageURL")
    public String largeImageURL;

    @Expose
    @SerializedName("fullHDURL")
    public String fullHdURL;

    @Expose
    @SerializedName("imageURL")
    public String imageURL;

    @Expose
    @SerializedName("likes")
    public int likes;

    @Expose
    @SerializedName("downloads")
    public int downloads;

    @Expose
    @SerializedName("views")
    public int views;
}
