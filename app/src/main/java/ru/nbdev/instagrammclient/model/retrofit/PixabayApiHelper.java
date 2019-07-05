package ru.nbdev.instagrammclient.model.retrofit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.instagrammclient.PixabaySearchFilter;
import ru.nbdev.instagrammclient.app.App;
import ru.nbdev.instagrammclient.model.entity.PhotosList;

public class PixabayApiHelper {
    private static final String API_KEY = "12680892-c0359658b65c6d9678e07788e";

    @Inject
    PixabayApiService api;

    public PixabayApiHelper() {
        App.getAppComponent().inject(this);
    }

    public Observable<PhotosList> requestRandomPhotosList() {
        return api.getPhotosList(API_KEY, 200).subscribeOn(Schedulers.io());
    }

    public Observable<PhotosList> requestPhotosList(String query, PixabaySearchFilter filter) {
        return api.getPhotosList(
                API_KEY,
                query,
                filter.getSelectedImageTypeKey(),
                filter.getSelectedCategoryKey(),
                filter.getSelectedOrderKey(),
                200
        ).subscribeOn(Schedulers.io());
    }
}
