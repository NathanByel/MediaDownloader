package ru.nbdev.mediadownloader.view.detail;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;

import ru.nbdev.mediadownloader.model.entity.Photo;

public interface DetailView extends MvpView {

    void showPhoto(Photo photo);

    void checkWriteStoragePermissions();

    void showMessage(@StringRes int textId);

    void showMessage(@StringRes int textId, String text);
}
