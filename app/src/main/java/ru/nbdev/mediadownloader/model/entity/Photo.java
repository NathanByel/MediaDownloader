package ru.nbdev.mediadownloader.model.entity;

public class Photo {
    public long id;
    public String previewURL;
    public String fullSizeURL;
    public int likes;
    public int views;


    public Photo() {
    }

    public Photo(long id, String previewURL, String fullSizeURL, int likes, int views) {
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
