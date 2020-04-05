package ru.nbdev.mediadownloader.view.main;

import androidx.annotation.StringRes;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndStrategy;
import moxy.viewstate.strategy.SkipStrategy;
import moxy.viewstate.strategy.StateStrategyType;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayFilter;

@StateStrategyType(AddToEndStrategy.class)
public interface MainView extends MvpView {

    @StateStrategyType(SkipStrategy.class)
    void runDetailActivity(long photoId);

    @StateStrategyType(SkipStrategy.class)
    void showMessage(@StringRes int textId);

    @StateStrategyType(SkipStrategy.class)
    void showMessage(@StringRes int textId, String text);

    void showFilterDialog(PixabayFilter filter);

    void updatePhotosList();

    void hideFilterDialog();

    void showProgress();

    void showResult();

    void showError();
}
