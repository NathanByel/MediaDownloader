package ru.nbdev.mediadownloader.model.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

import ru.nbdev.mediadownloader.model.room.converters.DateTypeConverter;
import ru.nbdev.mediadownloader.model.room.entity.DbJoinRequestAndPhotos;
import ru.nbdev.mediadownloader.model.room.entity.DbPhoto;
import ru.nbdev.mediadownloader.model.room.entity.DbSearchRequest;

@Dao
public abstract class PhotoCacheDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertRequest(DbSearchRequest dbSearchRequest);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertPhotos(List<DbPhoto> photos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertPhoto(DbPhoto photo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertJoinRequestAndPhotos(DbJoinRequestAndPhotos[] joinPhotos);

    @Transaction
    @Query("DELETE FROM photos_table " +
            "WHERE _id IN( " +
            "SELECT photos_tbl._id " +
            "FROM photos_table AS photos_tbl " +
            "LEFT JOIN join_request_and_photos_table AS join_tbl ON photos_tbl._id = join_tbl.photo_id " +
            "WHERE join_tbl.photo_id is NULL);")
    public abstract void deleteUnusedPhotos();

    @Query("DELETE FROM search_requests_table " +
            "WHERE date < :utcTime;")
    public abstract int deleteRequestsOlderThan(long utcTime);

    @Query("DELETE FROM search_requests_table " +
            "WHERE request == :request AND extra_data == :extraDataJSON")
    public abstract int deleteRequest(String request, String extraDataJSON);

    @Query("SELECT request_tbl.date " +
            "FROM search_requests_table AS request_tbl " +
            "WHERE request == :request AND extra_data == :extraDataJSON")
    @TypeConverters({DateTypeConverter.class})
    public abstract Date getRequestDate(String request, String extraDataJSON);

    @Query("SELECT join_tbl.photo_id AS photo_id " +
            "FROM search_requests_table AS request_tbl " +
            "JOIN join_request_and_photos_table AS join_tbl ON request_tbl._id = join_tbl.search_id " +
            "WHERE request == :request AND extra_data == :extraDataJSON")
    public abstract int[] getPhotosIds(String request, String extraDataJSON);

    @Query("SELECT * " +
            "FROM photos_table " +
            "WHERE _id IN (:photosId)")
    public abstract List<DbPhoto> getPhotosByIds(int[] photosId);

    @Query("SELECT * " +
            "FROM photos_table " +
            "WHERE _id IN (:photoId)")
    public abstract DbPhoto getPhotoById(long photoId);

    @Transaction
    public void insertRequestAndPhotos(DbSearchRequest dbSearchRequest, List<DbPhoto> photos) {
        long id = insertRequest(dbSearchRequest);
        long[] photosId = insertPhotos(photos);

        DbJoinRequestAndPhotos[] joinRecords = new DbJoinRequestAndPhotos[photosId.length];
        for (int i = 0; i < joinRecords.length; i++) {
            joinRecords[i] = new DbJoinRequestAndPhotos(id, photosId[i]);
        }
        insertJoinRequestAndPhotos(joinRecords);
    }

    @Transaction
    public int deleteRequestAndPhotosOlderThan(Date date) {
        int deleted = deleteRequestsOlderThan(date.getTime());
        deleteUnusedPhotos();
        return deleted;
    }

    @Transaction
    public List<DbPhoto> getPhotosByRequest(String request, String extraDataJSON) {
        int[] photosId = getPhotosIds(request, extraDataJSON);
        if (photosId.length == 0) {
            return null;
        }
        return getPhotosByIds(photosId);
    }
}
