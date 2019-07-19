package ru.nbdev.instagrammclient.view.detail;

import com.arellomobile.mvp.MvpView;

import ru.nbdev.instagrammclient.model.entity.Photo;

public interface DetailView extends MvpView {
    void showPhoto(Photo photo);

    void savePhoto(Photo photo);

    void sharePhoto(Photo photo);

    void checkWriteStoragePermissions();

    void showMessage(String path);
}
