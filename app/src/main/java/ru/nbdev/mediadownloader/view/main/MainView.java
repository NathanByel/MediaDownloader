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
    void runDetailActivity(int photoId);

    void updateRecyclerView();

    @StateStrategyType(SkipStrategy.class)
    void showMessage(@StringRes int textId);

    @StateStrategyType(SkipStrategy.class)
    void showMessage(@StringRes int textId, String text);

    void showFilterDialog(PixabayFilter filter);

    void hideFilterDialog();

    void showProgress();

    void hideProgress();

    void showError();
}
