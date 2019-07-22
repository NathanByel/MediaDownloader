package ru.nbdev.instagrammclient.model.retrofit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.instagrammclient.BuildConfig;
import ru.nbdev.instagrammclient.presenter.PixabaySearchFilter;
import ru.nbdev.instagrammclient.app.App;
import ru.nbdev.instagrammclient.model.entity.PhotosList;

public class PixabayApiHelper {

    @Inject
    PixabayApiService api;

    public PixabayApiHelper() {
        App.getAppComponent().inject(this);
    }

    public Observable<PhotosList> requestRandomPhotosList() {
        return api.getPhotosList(BuildConfig.PIXABAY_API_KEY, 200).subscribeOn(Schedulers.io());
    }

    public Observable<PhotosList> requestPhotosList(String query, PixabaySearchFilter filter) {
        return api.getPhotosList(
                BuildConfig.PIXABAY_API_KEY,
                query,
                filter.getSelectedImageTypeKey(),
                filter.getSelectedCategoryKey(),
                filter.getSelectedOrderKey(),
                200
        ).subscribeOn(Schedulers.io());
    }
}
