package ru.nbdev.mediadownloader.view.main;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayFilter;

public interface MainView extends MvpView {

    @StateStrategyType( SkipStrategy.class)
    void runDetailActivity(int photoId);

    void updateRecyclerView();

    @StateStrategyType( SkipStrategy.class)
    void showMessage(@StringRes int textId);

    @StateStrategyType( SkipStrategy.class)
    void showMessage(@StringRes int textId, String text);

    void showFilterDialog(PixabayFilter filter);

    void hideFilterDialog();

    void showProgress();

    void hideProgress();

    void showError();
}
