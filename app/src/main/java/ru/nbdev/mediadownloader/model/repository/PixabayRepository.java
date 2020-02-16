package ru.nbdev.mediadownloader.model.repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayApiPhoto;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabaySearchRequest;
import ru.nbdev.mediadownloader.model.network.ApiKeyProvider;
import ru.nbdev.mediadownloader.model.network.pixabay.PixabayApiService;

public class PixabayRepository implements PhotoRepository {

    private static final String serviceName = "pixabay.com";

    private final PixabayApiService api;
    private final ApiKeyProvider apiKeyProvider;

    public PixabayRepository(PixabayApiService api, ApiKeyProvider apiKeyProvider) {
        this.api = api;
        this.apiKeyProvider = apiKeyProvider;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public Single<Photo> getPhotoById(int id) {
        return api.getPhotoById(
                apiKeyProvider.getApiKey(),
                id
        )
                .subscribeOn(Schedulers.io())
                .map(pixabayApiPhotosList -> {
                    //FIXME need check hits to null
                    return mapApiPhotoToPhoto(pixabayApiPhotosList.hits.get(0));
                });
    }

    @Override
    public Single<List<Photo>> getRandomPhotos() {
        return api.getPhotosList(
                apiKeyProvider.getApiKey(),
                200
        )
                .subscribeOn(Schedulers.io())
                .map(pixabayApiPhotosList -> mapApiPhotosToPhotos(pixabayApiPhotosList.hits));
    }

    @Override
    public Single<List<Photo>> searchPhotos(SearchRequest request) {
        if (request instanceof PixabaySearchRequest) {
            PixabaySearchRequest pixabayRequest = (PixabaySearchRequest) request;
            return api.getPhotosList(
                    apiKeyProvider.getApiKey(),
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
                    apiKeyProvider.getApiKey(),
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
