package ru.nbdev.mediadownloader.model.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import ru.nbdev.mediadownloader.model.room.entity.DbPhoto;

@Dao
public interface PhotosDao {

    @Query("SELECT * FROM photos_table")
    Single<List<DbPhoto>> getPhotosList();

    @Query("SELECT * FROM photos_table WHERE _id = :id")
    Single<DbPhoto> getPhotoById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(DbPhoto photo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertList(List<DbPhoto> photos);

    @Update
    int update(DbPhoto photo);

    @Delete
    int delete(DbPhoto photo);
}
