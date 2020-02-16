package ru.nbdev.mediadownloader.model.cache;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;
import ru.nbdev.mediadownloader.model.room.AppDatabase;
import ru.nbdev.mediadownloader.model.room.entity.DbPhoto;
import ru.nbdev.mediadownloader.model.room.entity.DbSearchRequest;

public class RoomPhotoCacheProvider implements PhotoCacheProvider {

    private final AppDatabase database;

    public RoomPhotoCacheProvider(Context context, String cacheName) {
        database = Room.databaseBuilder(context, AppDatabase.class, "cache_" + cacheName).build();
    }

    @Override
    public int deleteRequestAndPhotosOlderThan(Date date) {
        return database.photoCacheDao().deleteRequestAndPhotosOlderThan(date);
    }

    @Override
    public Date getRequestDate(SearchRequest searchRequest) {
        return database.photoCacheDao().getRequestDate(
                mapSearchRequestToDbSearchRequest(searchRequest)
        );
    }

    @Override
    public void insertRequestAndPhotos(SearchRequest searchRequest, List<Photo> photos) {
        database.photoCacheDao().insertRequestAndPhotos(
                mapSearchRequestToDbSearchRequest(searchRequest),
                mapPhotosToDbPhotos(photos)
        );
    }

    @Override
    public List<Photo> getPhotosByRequest(SearchRequest searchRequest) {
        return mapDbPhotosToPhotos(
                database.photoCacheDao().getPhotosByRequest(
                        mapSearchRequestToDbSearchRequest(searchRequest)
                )
        );
    }

    @Override
    public Photo getPhoto(long id) {
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
}
