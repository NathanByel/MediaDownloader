package ru.nbdev.mediadownloader.view.detail;

import androidx.annotation.StringRes;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndStrategy;
import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.nbdev.mediadownloader.model.entity.Photo;

@StateStrategyType(AddToEndStrategy.class)
public interface DetailView extends MvpView {

    void showPhoto(Photo photo);

    @StateStrategyType(SkipStrategy.class)
    void checkWriteStoragePermissions();

    @StateStrategyType(SkipStrategy.class)
    void showMessage(@StringRes int textId);

    @StateStrategyType(SkipStrategy.class)
    void showMessage(@StringRes int textId, String text);

    void showError();
}
