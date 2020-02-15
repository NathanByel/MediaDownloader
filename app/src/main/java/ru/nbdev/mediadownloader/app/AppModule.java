package ru.nbdev.mediadownloader.app;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nbdev.mediadownloader.common.Constants;
import ru.nbdev.mediadownloader.model.repository.CachedPhotoRepository;
import ru.nbdev.mediadownloader.model.repository.PhotoRepository;
import ru.nbdev.mediadownloader.model.repository.PixabayRepository;
import ru.nbdev.mediadownloader.model.repository.RoomPhotoCacheRepository;
import ru.nbdev.mediadownloader.model.retrofit.PixabayApiService;

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    PixabayApiService providePixabayApi() {
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        return new Retrofit.Builder()
                .baseUrl(Constants.SERVER_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(PixabayApiService.class);
    }

    @Provides
    @Singleton
    PhotoRepository providePhotoRepository(PixabayApiService api) {
        PhotoRepository repository = new PixabayRepository(api);
        return new CachedPhotoRepository(
                new RoomPhotoCacheRepository(application, repository.getServiceName()),
                repository
        );
    }
}
