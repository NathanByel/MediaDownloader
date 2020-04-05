package ru.nbdev.mediadownloader.model.network.pixabay;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayApiPhotosList;

public interface PixabayApiService {

    @GET("api")
    Single<PixabayApiPhotosList> getRandomPhotosList(
            @Query("key") String key,
            @Query("safesearch") boolean safesearch,
            @Query("per_page") int perPage
    );

    @GET("api")
    Single<PixabayApiPhotosList> getPhotosList(
            @Query("key") String key,
            @Query("safesearch") boolean safesearch,
            @Query("q") String query,
            @Query("image_type") String imageType,
            @Query("category") String category,
            @Query("order") String order,
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    @GET("api")
    Single<PixabayApiPhotosList> getPhotoById(
            @Query("key") String key,
            @Query("id") long id
    );
}
