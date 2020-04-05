package ru.nbdev.mediadownloader.model.network.pixabay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nbdev.mediadownloader.model.network.HostProvider;

public class PixabayApi {

    private final HostProvider hostProvider;


    public PixabayApi(HostProvider hostProvider) {
        this.hostProvider = hostProvider;
    }

    public PixabayApiService getService() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        return new Retrofit.Builder()
                .baseUrl(hostProvider.getHostUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(PixabayApiService.class);
    }
}
