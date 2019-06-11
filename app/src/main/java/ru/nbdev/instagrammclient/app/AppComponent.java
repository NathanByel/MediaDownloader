package ru.nbdev.instagrammclient.app;

import javax.inject.Singleton;

import dagger.Component;
import ru.nbdev.instagrammclient.presenter.DetailPresenter;
import ru.nbdev.instagrammclient.presenter.MainPresenter;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MainPresenter mainPresenter);

    void inject(DetailPresenter detailPresenter);
}
