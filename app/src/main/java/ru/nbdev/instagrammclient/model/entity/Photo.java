package ru.nbdev.instagrammclient.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {
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
