package ru.nbdev.mediadownloader.app;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.nbdev.mediadownloader.common.Constants;
import ru.nbdev.mediadownloader.common.image_loader.GlideImageLoader;
import ru.nbdev.mediadownloader.common.image_loader.ImageLoader;
import ru.nbdev.mediadownloader.common.media_manager.AndroidMediaManager;
import ru.nbdev.mediadownloader.common.media_manager.MediaManager;
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
        return new CachedPhotoRepository(
                photoCacheProvider,
                repository,
                Constants.CACHE_LIFETIME,
                Constants.CACHE_LIFETIME_UNITS
        );
    }

    @Provides
    @Singleton
    MediaManager provideMediaManager() {
        return new AndroidMediaManager(application);
    }

    @Provides
    @Singleton
    ImageLoader provideImageLoader() {
        return new GlideImageLoader(application);
    }
}
