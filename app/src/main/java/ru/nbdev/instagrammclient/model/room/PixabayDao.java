package ru.nbdev.instagrammclient.model.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Single;
import ru.nbdev.instagrammclient.model.entity.Photo;
import ru.nbdev.instagrammclient.model.entity.SearchRequest;

@Dao
public interface PixabayDao {

    @Query("SELECT * FROM table_pixabay_photos_list")
    Single<List<Photo>> getPhotosList();

    @Query("SELECT * FROM table_pixabay_photos_list WHERE id = :id")
    Single<Photo> getPhotoById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Photo photo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertList(List<Photo> photos);

    @Update
    int update(Photo photo);

    @Delete
    int delete(Photo photo);



    @Query("SELECT * FROM table_photo_search_cache WHERE request = :request")
    Single<SearchRequest> findSearchRequest(String request);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SearchRequest searchRequest);

    @Delete
    int delete(SearchRequest searchRequest);
}
