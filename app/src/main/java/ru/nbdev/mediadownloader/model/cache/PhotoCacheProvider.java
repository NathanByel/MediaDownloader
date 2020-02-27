package ru.nbdev.mediadownloader.model.cache;

import java.util.Date;
import java.util.List;

import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;

public interface PhotoCacheProvider {

    int deleteRecordsOlderThan(Date date);

    Date getRecordDate(SearchRequest request);

    void insertRecord(CacheRecord record);

    void deleteRecord(SearchRequest request);

    List<Photo> getPhotosByRequest(SearchRequest request);

    Photo getPhotoById(long id);
}
