package ru.nbdev.mediadownloader.model.repository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayApiPhoto;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabaySearchRequest;
import ru.nbdev.mediadownloader.model.network.ApiKeyProvider;
import ru.nbdev.mediadownloader.model.network.pixabay.PixabayApiService;

public class PixabayRepository implements PhotoRepository {

    private static final String SERVICE_NAME = "pixabay.com";
    private static final boolean SAFE_SEARCH = true;
    private final PixabayApiService api;
    private final ApiKeyProvider apiKeyProvider;


    public PixabayRepository(PixabayApiService api, ApiKeyProvider apiKeyProvider) {
        this.api = api;
        this.apiKeyProvider = apiKeyProvider;
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    public Single<Photo> getPhotoById(long id) {
        return api.getPhotoById(
                apiKeyProvider.getApiKey(),
                id
        )
                .map(pixabayApiPhotosList -> mapApiPhotoToPhoto(pixabayApiPhotosList.hits.get(0)));
    }

    @Override
    public Single<List<Photo>> getRandomPhotos() {
        return api.getRandomPhotosList(
                apiKeyProvider.getApiKey(),
                SAFE_SEARCH,
                200
        )
                .map(pixabayApiPhotosList -> mapApiPhotosToPhotos(pixabayApiPhotosList.hits));
    }

    @Override
    public Single<List<Photo>> searchPhotos(SearchRequest request) {
        if (request instanceof PixabaySearchRequest) {
            PixabaySearchRequest pixabayRequest = (PixabaySearchRequest) request;
            return api.getPhotosList(
                    apiKeyProvider.getApiKey(),
                    SAFE_SEARCH,
                    pixabayRequest.getRequest(),
                    pixabayRequest.typeKey(),
                    pixabayRequest.categoryKey(),
                    pixabayRequest.orderKey(),
                    1,
                    200
            )
                    .map(pixabayApiPhotosList -> mapApiPhotosToPhotos(pixabayApiPhotosList.hits));
        } else {
            return getRandomPhotos();
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
