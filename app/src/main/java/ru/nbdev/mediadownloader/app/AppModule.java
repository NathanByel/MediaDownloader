package ru.nbdev.mediadownloader.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.nbdev.mediadownloader.common.AndroidMediaManager;
import ru.nbdev.mediadownloader.common.MediaManager;
import ru.nbdev.mediadownloader.model.cache.PhotoCacheProvider;
import ru.nbdev.mediadownloader.model.cache.RoomPhotoCacheProvider;
import ru.nbdev.mediadownloader.model.network.ApiKeyProvider;
import ru.nbdev.mediadownloader.model.network.HostProvider;
import ru.nbdev.mediadownloader.model.network.pixabay.PixabayApi;
import ru.nbdev.mediadownloader.model.network.pixabay.PixabayApiKeyProvider;
import ru.nbdev.mediadownloader.model.network.pixabay.PixabayHostProvider;
import ru.nbdev.mediadownloader.model.repository.CachedPhotoRepository;
import ru.nbdev.mediadownloader.model.repository.PhotoRepository;
import ru.nbdev.mediadownloader.model.repository.PixabayRepository;

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    PhotoRepository providePhotoRepository() {
        HostProvider hostProvider = new PixabayHostProvider();
        ApiKeyProvider apiKeyProvider = new PixabayApiKeyProvider();
        PixabayApi pixabayApi = new PixabayApi(hostProvider);
        PhotoRepository repository = new PixabayRepository(pixabayApi.getService(), apiKeyProvider);
        PhotoCacheProvider photoCacheProvider = new RoomPhotoCacheProvider(application, repository.getServiceName());
        return new CachedPhotoRepository(photoCacheProvider, repository);
    }

    @Provides
    @Singleton
    MediaManager provideMediaManager() {
        return new AndroidMediaManager(application);
    }
}
