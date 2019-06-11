package ru.nbdev.instagrammclient.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.instagrammclient.app.App;
import ru.nbdev.instagrammclient.model.room.AppDatabase;
import ru.nbdev.instagrammclient.view.detail.DetailView;

@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {
    private static final String TAG = "DetailPresenter";

    private int photoId;

    @Inject
    AppDatabase database;

    public DetailPresenter() {
        App.getAppComponent().inject(this);
    }

    public void onNewPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public void onImageClick() {
        Log.d(TAG, "Photo ID = " + photoId);
    }

    @Override
    protected void onFirstViewAttach() {
        if (photoId > -1) {
            Disposable disposable = database.pixabayDao().getPhotoById(photoId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            photo -> getViewState().setDetailPhoto(photo.largeImageURL),
                            throwable -> Log.e(TAG, "Error, " + throwable)
                    );
        }
    }
}
