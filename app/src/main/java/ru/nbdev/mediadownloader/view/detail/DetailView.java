package ru.nbdev.mediadownloader.view.detail;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.nbdev.mediadownloader.model.entity.Photo;

public interface DetailView extends MvpView {

    void showPhoto(Photo photo);

    @StateStrategyType( SkipStrategy.class)
    void checkWriteStoragePermissions();

    @StateStrategyType( SkipStrategy.class)
    void showMessage(@StringRes int textId);

    @StateStrategyType( SkipStrategy.class)
    void showMessage(@StringRes int textId, String text);

    void showError();
}
