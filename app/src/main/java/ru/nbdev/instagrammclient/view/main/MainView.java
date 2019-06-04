package ru.nbdev.instagrammclient.view.main;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

@StateStrategyType(SkipStrategy.class)
public interface MainView extends MvpView {
    void runDetailActivity(String detailURL);

    void updateRecyclerView();

    void showMessage(String text);
}
