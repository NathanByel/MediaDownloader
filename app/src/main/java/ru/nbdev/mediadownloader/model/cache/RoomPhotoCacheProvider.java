package ru.nbdev.mediadownloader.model.cache;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;
import ru.nbdev.mediadownloader.model.room.AppDatabase;
import ru.nbdev.mediadownloader.model.room.converters.MapTypeConverter;
import ru.nbdev.mediadownloader.model.room.entity.DbPhoto;
import ru.nbdev.mediadownloader.model.room.entity.DbSearchRequest;

public class RoomPhotoCacheProvider implements PhotoCacheProvider {

    private final AppDatabase database;

    public RoomPhotoCacheProvider(Context context, String cacheName) {
        database = Room.databaseBuilder(context, AppDatabase.class, "cache_" + cacheName).build();
    }

    @Override
    public int deleteRecordsOlderThan(Date date) {
        return database.photoCacheDao().deleteRequestAndPhotosOlderThan(date);
    }

    @Override
    public Date getRecordDate(SearchRequest request) {
        return database.photoCacheDao().getRequestDate(
                request.getRequest(),
                extraDataToJson(request.getExtraData())
        );
    }

    @Override
    public void insertRecord(CacheRecord record) {
        database.photoCacheDao().insertRequestAndPhotos(
                mapSearchRequestToDbSearchRequest(record.getSearchRequest()),
                mapPhotosToDbPhotos(record.getPhotos())
        );
    }

    @Override
    public void deleteRecord(SearchRequest request) {
        database.photoCacheDao().deleteRequest(
                request.getRequest(),
                extraDataToJson(request.getExtraData())
        );
    }

    @Override
    public List<Photo> getPhotosByRequest(SearchRequest searchRequest) {
        return mapDbPhotosToPhotos(
                database.photoCacheDao().getPhotosByRequest(
                        searchRequest.getRequest(),
                        extraDataToJson(searchRequest.getExtraData())
                )
        );
    }

    @Override
    public Photo getPhotoById(long id) {
        return mapDbPhotoToPhoto(
                database.photoCacheDao().getPhotoById(id)
        );
    }

    private List<Photo> mapDbPhotosToPhotos(List<DbPhoto> dbPhotos) {
        List<Photo> list = new ArrayList<>(dbPhotos.size());
        for (DbPhoto dbPhoto : dbPhotos) {
            list.add(mapDbPhotoToPhoto(dbPhoto));
        }
        return list;
    }

    private List<DbPhoto> mapPhotosToDbPhotos(List<Photo> photos) {
        List<DbPhoto> list = new ArrayList<>(photos.size());
        for (Photo photo : photos) {
            list.add(mapDbPhotoToPhoto(photo));
        }
        return list;
    }

    private Photo mapDbPhotoToPhoto(DbPhoto dbPhoto) {
        if (dbPhoto == null) {
            return null;
        }
        return new Photo(
                dbPhoto.getId(),
                dbPhoto.getPreviewURL(),
                dbPhoto.getFullSizeURL(),
                dbPhoto.getLikes(),
                dbPhoto.getViews()
        );
    }

    private DbPhoto mapDbPhotoToPhoto(Photo photo) {
        if (photo == null) {
            return null;
        }
        return new DbPhoto(
                photo.getId(),
                photo.getPreviewURL(),
                photo.getFullSizeURL(),
                photo.getLikes(),
                photo.getViews()
        );
    }

    private DbSearchRequest mapSearchRequestToDbSearchRequest(SearchRequest request) {
        return new DbSearchRequest(request.getRequest(), request.getExtraData());
    }

    public String extraDataToJson(Map<String, Object> extraData) {
        return MapTypeConverter.fromMap(extraData);
    }
}
