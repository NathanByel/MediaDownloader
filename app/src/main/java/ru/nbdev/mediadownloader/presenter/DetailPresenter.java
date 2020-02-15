package ru.nbdev.mediadownloader.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.repository.PhotoRepository;
import ru.nbdev.mediadownloader.view.detail.DetailView;
import timber.log.Timber;

@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {

    private final int photoId;
    private Photo photo;

    @Inject
    PhotoRepository photoRepository;

    public DetailPresenter(int photoId) {
        this.photoId = photoId;
    }

    public void onSaveClick() {
        Timber.d("Photo ID = %d", photoId);
        getViewState().checkWriteStoragePermissions();
    }

    @Override
    protected void onFirstViewAttach() {
        if (photo == null) {
            loadPhoto(photoId);
        } else {
            getViewState().showPhoto(photo);
        }
    }

    private void loadPhoto(int id) {
        Disposable disposable = photoRepository.getPhotoById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        photo -> {
                            this.photo = photo;
                            getViewState().showPhoto(photo);
                        },
                        throwable -> {
                            Timber.e("loadPhoto() error. %s", throwable.getMessage());
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
