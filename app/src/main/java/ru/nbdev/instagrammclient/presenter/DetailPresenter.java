package ru.nbdev.instagrammclient.presenter;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.instagrammclient.R;
import ru.nbdev.instagrammclient.model.entity.Photo;
import ru.nbdev.instagrammclient.model.room.AppDatabase;
import ru.nbdev.instagrammclient.view.detail.DetailView;

@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {

    private static final String TAG = "DetailPresenter";

    private int photoId;
    private Photo photo;

    @Inject
    AppDatabase database;

    public DetailPresenter(int photoId) {
        this.photoId = photoId;
    }

    public void onSaveClick() {
        Log.d(TAG, "Photo ID = " + photoId);
        getViewState().checkWriteStoragePermissions();
    }

    @Override
    protected void onFirstViewAttach() {
        if (photo == null) {
            loadPhotoFromDb();
        } else {
            getViewState().showPhoto(photo);
        }
    }

    private void loadPhotoFromDb() {
        Disposable disposable = database.pixabayDao().getPhotoById(photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        photo -> {
                            this.photo = photo;
                            getViewState().showPhoto(photo);
                        },
                        throwable -> {
                            Log.e(TAG, "Error, " + throwable);
                            getViewState().showMessage(R.string.load_error);
                        }
                );
    }

    public void onWriteStoragePermissionGranted() {
        if (photo != null) {
            getViewState().savePhoto(photo);
        } else {
            getViewState().showMessage(R.string.load_error);
        }
    }

    public void onShareClick() {
        if (photo != null) {
            getViewState().sharePhoto(photo);
        } else {
            getViewState().showMessage(R.string.load_error);
        }
    }
}
