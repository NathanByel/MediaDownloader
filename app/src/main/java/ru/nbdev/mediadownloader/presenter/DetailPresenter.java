package ru.nbdev.mediadownloader.presenter;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.common.MediaManager;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.repository.PhotoRepository;
import ru.nbdev.mediadownloader.view.detail.DetailView;
import timber.log.Timber;

@InjectViewState
public class DetailPresenter extends MvpPresenter<DetailView> {

    private final int photoId;
    private Photo photo;
    private CompositeDisposable compositeDisposable;

    @Inject
    MediaManager mediaManager;

    @Inject
    PhotoRepository photoRepository;

    public DetailPresenter(int photoId) {
        this.photoId = photoId;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onFirstViewAttach() {
        if (photo == null) {
            loadPhotoById(photoId);
        } else {
            getViewState().showPhoto(photo);
        }
        Timber.d("Photo ID = %d", photoId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void onSaveClick() {
        if (photo == null) {
            getViewState().showMessage(R.string.load_error);
            return;
        }
        getViewState().checkWriteStoragePermissions();
    }

    public void onShareClick() {
        if (photo == null) {
            getViewState().showMessage(R.string.load_error);
            return;
        }
        mediaManager.sharePhoto(photo);
    }

    public void onWriteStoragePermissionReady(boolean isGranted) {
        if (isGranted) {
            Disposable disposable = mediaManager.downloadPhoto(photo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(file -> {
                        mediaManager.updateAndroidGallery(file);
                        getViewState().showMessage(R.string.saved_to, " " + file.toString());
                    }, throwable -> getViewState().showMessage(R.string.save_error));
            compositeDisposable.add(disposable);
        } else {
            getViewState().showMessage(R.string.storage_permission_needed);
        }
    }

    private void loadPhotoById(int id) {
        Disposable disposable = photoRepository.getPhotoById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        photo -> {
                            this.photo = photo;
                            getViewState().showPhoto(photo);
                        },
                        throwable -> {
                            Timber.e("loadPhotoById() error. %s", throwable.getMessage());
                            getViewState().showError();
                        }
                );
        compositeDisposable.add(disposable);
    }
}
