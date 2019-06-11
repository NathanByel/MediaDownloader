package ru.nbdev.instagrammclient.model.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nbdev.instagrammclient.model.entity.PhotosList;

public class PixabayApiHelper {
    private static final String API_KEY = "12680892-c0359658b65c6d9678e07788e";

    public Observable<PhotosList> requestPhotosList() {
        //http://pixabay.com/api/?key=12680892-c0359658b65c6d9678e07788e
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        PixabayApiService api = new Retrofit.Builder()
                .baseUrl("https://pixabay.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(PixabayApiService.class);

        return api.getPhotosList(API_KEY, 200).subscribeOn(Schedulers.io());
    }
}
