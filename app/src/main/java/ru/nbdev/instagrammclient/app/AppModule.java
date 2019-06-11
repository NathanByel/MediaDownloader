package ru.nbdev.instagrammclient.app;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.nbdev.instagrammclient.model.retrofit.PixabayApiHelper;
import ru.nbdev.instagrammclient.model.room.AppDatabase;

@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    PixabayApiHelper providePixabayApiHelper() {
        return new PixabayApiHelper();
    }

    @Provides
    @Singleton
    AppDatabase provideDatabase() {
        return Room.databaseBuilder(application, AppDatabase.class, "database").build();
    }
}
