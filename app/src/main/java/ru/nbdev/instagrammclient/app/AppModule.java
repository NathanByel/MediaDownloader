package ru.nbdev.instagrammclient.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nbdev.instagrammclient.model.Constants;
import ru.nbdev.instagrammclient.model.retrofit.PixabayApiService;
import ru.nbdev.instagrammclient.model.room.AppDatabase;

@Module
public class AppModule {

    private Application application;

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
    AppDatabase provideDatabase() {
        return Room.databaseBuilder(application, AppDatabase.class, "database").build();
    }
}
