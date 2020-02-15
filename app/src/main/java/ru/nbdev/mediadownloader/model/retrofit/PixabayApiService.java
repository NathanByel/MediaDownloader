package ru.nbdev.mediadownloader.model.retrofit;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayApiPhotosList;

public interface PixabayApiService {

    @GET("api")
    Single<PixabayApiPhotosList> getPhotosList(
            @Query("key") String key,
            @Query("per_page") int perPage
    );

    @GET("api")
    Single<PixabayApiPhotosList> getPhotosList(
            @Query("key") String key,
            @Query("q") String query,
            @Query("image_type") String imageType,
            @Query("category") String category,
            @Query("order") String order,
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    @GET("api")
    Observable<PixabayApiPhotosList> getById(
            @Query("key") String key,
            @Query("id") int id
    );
}
