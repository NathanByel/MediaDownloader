package ru.nbdev.instagrammclient.model.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nbdev.instagrammclient.model.entity.PhotosList;

public interface PixabayApiService {

    @GET("api")
    Observable<PhotosList> getPhotosList(
            @Query("key") String key,
            @Query("per_page") int perPage
    );

    @GET("api")
    Observable<PhotosList> getPhotosList(
            @Query("key") String key,
            @Query("q") String query,
            @Query("image_type") String imageType,
            @Query("category") String category,
            @Query("order") String order,
            @Query("per_page") int perPage
    );

    @GET("api")
    Observable<PhotosList> getById(@Query("key") String key, @Query("id") int id);
}
