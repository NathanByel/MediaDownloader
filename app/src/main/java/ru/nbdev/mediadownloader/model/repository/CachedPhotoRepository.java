package ru.nbdev.mediadownloader.model.repository;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.mediadownloader.model.cache.CacheRecord;
import ru.nbdev.mediadownloader.model.cache.PhotoCacheProvider;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;
import timber.log.Timber;

public class CachedPhotoRepository implements PhotoRepository {

    private final PhotoCacheProvider cache;
    private final PhotoRepository photoRepository;
    private final int cacheLifeTime;
    private final int cacheLifeTimeUnits;


    public CachedPhotoRepository(PhotoCacheProvider cache, PhotoRepository photoRepository, int cacheLifeTime, int cacheLifeTimeUnits) {
        this.cache = cache;
        this.photoRepository = photoRepository;
        this.cacheLifeTime = cacheLifeTime;
        this.cacheLifeTimeUnits = cacheLifeTimeUnits;

        Disposable disposable = Single.fromCallable(() -> deleteOldRecords())
                .subscribeOn(Schedulers.io())
                .subscribe(deleted -> {
                    Timber.d("Auto drop old cache %s. Deleted records %d", photoRepository.getServiceName(), deleted);
                }, throwable -> {
                    Timber.e(throwable, "Auto drop old cache error.");
                });
    }

    @Override
    public String getServiceName() {
        return photoRepository.getServiceName();
    }

    @Override
    public Single<Photo> getPhotoById(long id) {
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
        return Maybe.concat(loadPhotosFromCache(request), loadPhotosFromInternet(request))
                .filter(photos -> photos != null && !photos.isEmpty())
                .first(Collections.emptyList());
    }

    private Maybe<List<Photo>> loadPhotosFromCache(SearchRequest request) {
        return Maybe.create(emitter -> {
            try {
                Date date = cache.getRecordDate(request);
                if (date == null) {
                    Timber.d("No record in cache");
                    emitter.onComplete();
                    return;
                }
                if (date.before(getRecordLifeEndDate())) {
                    cache.deleteRecord(request);
                    Timber.d("Old record in cache.");
                    emitter.onComplete();
                    return;
                }

                List<Photo> photos = cache.getPhotosByRequest(request);
                emitter.onSuccess(photos);
            } catch (Exception e) {
                Timber.e(e, "CachedPhotoRepository loadPhotosFromCache() error.");
                emitter.onError(e);
            }
        });
    }

    private Maybe<List<Photo>> loadPhotosFromInternet(SearchRequest request) {
        return photoRepository.searchPhotos(request)
                .doOnSuccess(photos -> saveRequestToCache(request, photos))
                .doOnError(throwable -> Timber.e(throwable, "CachedPhotoRepository loadPhotosFromInternet() error."))
                .toMaybe();
    }

    @SuppressWarnings("Convert2MethodRef")
    private Single<Photo> loadPhoto(long id) {
        return Maybe.concat(loadPhotoFromCache(id), loadPhotoFromInternet(id))
                .filter(photo -> photo != null)
                .first(new Photo());
    }

    private Maybe<Photo> loadPhotoFromCache(long id) {
        return Maybe.create(emitter -> {
            try {
                Photo photo = cache.getPhotoById(id);
                if (photo == null) {
                    Timber.d("No photo in cache");
                    emitter.onComplete();
                    return;
                }
                emitter.onSuccess(photo);
            } catch (Exception e) {
                Timber.e(e, "CachedPhotoRepository loadPhotoFromCache() error.");
                emitter.onError(e);
            }
        });
    }

    private Maybe<Photo> loadPhotoFromInternet(long id) {
        return Maybe.fromSingle(photoRepository.getPhotoById(id)
                .doOnSuccess(photo -> {
                    //TODO Подумать, надо-ли сохранять в кэш отдельные фото
                    Timber.d("loadPhotoFromInternet()");
                })
                .doOnError(throwable -> Timber.e(throwable, "CachedPhotoRepository loadPhotoFromInternet() error."))
        );
    }

    private void saveRequestToCache(SearchRequest request, List<Photo> photos) {
        cache.insertRecord(new CacheRecord(request, photos));
    }

    private int deleteOldRecords() {
        Date endDate = getRecordLifeEndDate();
        int deleted = cache.deleteRecordsOlderThan(endDate);
        Timber.d("Drop old cache %s. Deleted records %d", photoRepository.getServiceName(), deleted);
        return deleted;
    }

    private Date getRecordLifeEndDate() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(cacheLifeTimeUnits, -cacheLifeTime);
        return calendar.getTime();
    }
}
