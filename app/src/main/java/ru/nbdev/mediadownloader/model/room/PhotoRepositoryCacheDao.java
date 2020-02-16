package ru.nbdev.mediadownloader.model.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

import ru.nbdev.mediadownloader.model.room.converters.DateTypeConverter;
import ru.nbdev.mediadownloader.model.room.entity.DbJoinRequestAndPhotos;
import ru.nbdev.mediadownloader.model.room.entity.DbPhoto;
import ru.nbdev.mediadownloader.model.room.entity.DbSearchRequest;

@Dao
public abstract class PhotoRepositoryCacheDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertRequest(DbSearchRequest dbSearchRequest);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertPhotos(List<DbPhoto> photos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertJoinRequestAndPhotos(DbJoinRequestAndPhotos[] joinPhotos);

    @Query("SELECT request_tbl.date " +
            "FROM search_requests_table AS request_tbl " +
            "WHERE request == :request AND extra_data == :extraData")
    @TypeConverters({DateTypeConverter.class})
    public abstract Date getRequestDate(String request, String extraData);

    @Query("SELECT " +
            "join_tbl.photo_id AS photo_id " +
            "FROM search_requests_table AS request_tbl " +
            "JOIN join_request_and_photos_table AS join_tbl ON request_tbl._id = join_tbl.search_id " +
            "WHERE request == :request AND extra_data == :extraData")
    public abstract int[] getPhotosIdByRequest(String request, String extraData);

    @Query("SELECT * " +
            "FROM photos_table " +
            "WHERE _id IN (:photosId)")
    public abstract List<DbPhoto> getPhotosById(int[] photosId);

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
    public List<DbPhoto> getPhotosByRequest(DbSearchRequest request) {
        int[] photosId = getPhotosIdByRequest(request.request, request.getJsonExtraData());
        if (photosId.length == 0) {
            return null;
        }
        return getPhotosById(photosId);
    }

    public Date getRequestDate(DbSearchRequest request) {
        return getRequestDate(request.request, request.getJsonExtraData());
    }

    //    /*
//    delete photo without search id
//    SELECT
//        *
//    FROM
//        table_photos_list AS photos_tbl
//    LEFT JOIN
//        table_request_photos_list AS index_tbl ON photos_tbl.id = index_tbl.photo_id
//    WHERE
//        index_tbl.photo_id is NULL
//    ;
//    */
}
