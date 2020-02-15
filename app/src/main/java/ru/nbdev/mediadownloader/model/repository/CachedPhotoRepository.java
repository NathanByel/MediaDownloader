package ru.nbdev.mediadownloader.model.repository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;
import timber.log.Timber;

public class CachedPhotoRepository implements PhotoRepository {

    private PhotoCacheRepository cache;
    private PhotoRepository photoRepository;

    public CachedPhotoRepository(PhotoCacheRepository cache, PhotoRepository photoRepository) {
        this.cache = cache;
        this.photoRepository = photoRepository;
    }

    @Override
    public String getServiceName() {
        return photoRepository.getServiceName();
    }

    @Override
    public Single<Photo> getPhotoById(int id) {
        return loadPhoto(id);
    }

    @Override
    public Single<List<Photo>> getRandomPhotos() {
        return loadPhotos(new SearchRequest(""));
    }

    @Override
    public Single<List<Photo>> searchPhotos(SearchRequest request) {
        return loadPhotos(request);
    }

    private Single<List<Photo>> loadPhotos(SearchRequest request) {
        return Maybe.concat(loadFromCache(request), loadFromInternet(request))
                .filter(photos -> photos != null && !photos.isEmpty())
                .first(Collections.emptyList());
    }

    private Maybe<List<Photo>> loadFromCache(SearchRequest request) {
        return Maybe.create(emitter -> {
            try {
                Date date = cache.getRequestDate(request);
                if (date == null) {
                    Timber.i("No request in cache");
                    emitter.onComplete();
                    return;
                }
                List<Photo> photos = cache.getPhotosByRequest(request);
                emitter.onSuccess(photos);
            } catch (Exception e) {
                Timber.e("loadFromCache() error. %s", e.getMessage());
                emitter.onError(e);
            }
        });
    }

    private Maybe<List<Photo>> loadFromInternet(SearchRequest request) {
        return photoRepository.searchPhotos(request)
                .doOnSuccess(photos -> {
                    saveRequestToCache(request, photos);
                })
                .doOnError(throwable -> {
                    Timber.e("loadFromInternet() error. %s", throwable.getMessage());
                })
                .toMaybe();
    }

    private Single<Photo> loadPhoto(int id) {
        return Maybe.concat(loadPhotoFromCache(id), loadPhotoFromInternet(id))
                .filter(photo -> photo != null)
                .first(new Photo());
    }

    private Maybe<Photo> loadPhotoFromCache(int id) {
        return Maybe.create(emitter -> {
            try {
                Photo photo = cache.getPhoto(id);
                if (photo == null) {
                    Timber.i("No photo in cache");
                    emitter.onComplete();
                    return;
                }
                emitter.onSuccess(photo);
            } catch (Exception e) {
                Timber.e("loadPhotoFromCache() error. %s", e.getMessage());
                emitter.onError(e);
            }
        });
    }

    private Maybe<Photo> loadPhotoFromInternet(int id) {
        return Maybe.fromSingle(photoRepository.getPhotoById(id)
                .doOnSuccess(photo -> {
                    //FIXME saveRequestToCache(request, photo);
                    Timber.e("loadPhotoFromInternet()");
                })
                .doOnError(throwable -> {
                    Timber.e("loadPhotoFromInternet() error. %s", throwable.getMessage());
                }));
    }

    private void saveRequestToCache(SearchRequest request, List<Photo> photos) {
        cache.insertRequestAndPhotos(request, photos);
    }
}
