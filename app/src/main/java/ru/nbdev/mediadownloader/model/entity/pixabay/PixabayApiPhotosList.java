package ru.nbdev.mediadownloader.model.entity.pixabay;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PixabayApiPhotosList {
    @Expose
    @SerializedName("totalHits")
    public int totalHits;

    @Expose
    @SerializedName("hits")
    public List<PixabayApiPhoto> hits;
}
