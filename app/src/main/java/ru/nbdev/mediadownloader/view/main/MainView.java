package ru.nbdev.mediadownloader.view.main;

import android.support.annotation.StringRes;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayFilter;

@StateStrategyType(SkipStrategy.class)
public interface MainView extends MvpView {

    void runDetailActivity(int photoId);

    void updateRecyclerView();

    void showMessage(@StringRes int textId);

    void showPhotosCount(int count);

    void showFilterDialog(PixabayFilter filter);

    void showProgress();

    void hideProgress();
}
