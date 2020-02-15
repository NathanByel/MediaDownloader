package ru.nbdev.mediadownloader.model.repository;

import java.util.Date;
import java.util.List;

import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;

public interface PhotoCacheRepository {
    Date getRequestDate(SearchRequest searchRequest);

    void insertRequestAndPhotos(SearchRequest searchRequest, List<Photo> photos);

    List<Photo> getPhotosByRequest(SearchRequest request);

    Photo getPhoto(long id);
}
