package ru.nbdev.mediadownloader.model.repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.mediadownloader.BuildConfig;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayApiPhoto;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabaySearchRequest;
import ru.nbdev.mediadownloader.model.retrofit.PixabayApiService;

public class PixabayRepository implements PhotoRepository {

    private static final String serviceName = "pixabay.com";

    private PixabayApiService api;

    public PixabayRepository(PixabayApiService api) {
        this.api = api;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public Single<Photo> getPhotoById(int id) {
        return Single.fromObservable(
                api.getById(BuildConfig.PIXABAY_API_KEY, id)
                        .subscribeOn(Schedulers.io())
                        .map(pixabayApiPhotosList -> {
                            //FIXME
                            return mapApiPhotoToPhoto(pixabayApiPhotosList.hits.get(0));
                        })
        );
    }

    @Override
    public Single<List<Photo>> getRandomPhotos() {
        return api.getPhotosList(BuildConfig.PIXABAY_API_KEY, 200)
                .subscribeOn(Schedulers.io())
                .map(pixabayApiPhotosList -> mapApiPhotosToPhotos(pixabayApiPhotosList.hits));
    }

    @Override
    public Single<List<Photo>> searchPhotos(SearchRequest request) {
        if (request instanceof PixabaySearchRequest) {
            PixabaySearchRequest pixabayRequest = (PixabaySearchRequest) request;
            return api.getPhotosList(
                    BuildConfig.PIXABAY_API_KEY,
                    pixabayRequest.getRequest(),
                    pixabayRequest.typeKey(),
                    pixabayRequest.categoryKey(),
                    pixabayRequest.orderKey(),
                    1,
                    200
            )
                    .subscribeOn(Schedulers.io())
                    .map(pixabayApiPhotosList -> mapApiPhotosToPhotos(pixabayApiPhotosList.hits));
        } else {
            return api.getPhotosList(
                    BuildConfig.PIXABAY_API_KEY,
                    200
            ).subscribeOn(Schedulers.io())
                    .map(pixabayApiPhotosList -> mapApiPhotosToPhotos(pixabayApiPhotosList.hits));
        }
    }

    private List<Photo> mapApiPhotosToPhotos(List<PixabayApiPhoto> apiPhotos) {
        List<Photo> list = new ArrayList<>(apiPhotos.size());
        for (PixabayApiPhoto apiPhoto : apiPhotos) {
            list.add(mapApiPhotoToPhoto(apiPhoto));
        }
        return list;
    }

    private Photo mapApiPhotoToPhoto(PixabayApiPhoto apiPhoto) {
        return new Photo(
                apiPhoto.id,
                apiPhoto.previewURL,
                apiPhoto.largeImageURL,
                apiPhoto.likes,
                apiPhoto.views
        );
    }
}
