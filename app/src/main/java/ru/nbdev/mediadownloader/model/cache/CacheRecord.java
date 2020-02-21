package ru.nbdev.mediadownloader.model.cache;

import java.util.List;

import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;

public class CacheRecord {
    private SearchRequest searchRequest;
    private List<Photo> photos;

    public CacheRecord(SearchRequest searchRequest, List<Photo> photos) {
        this.searchRequest = searchRequest;
        this.photos = photos;
    }

    public SearchRequest getSearchRequest() {
        return searchRequest;
    }

    public List<Photo> getPhotos() {
        return photos;
    }
}
