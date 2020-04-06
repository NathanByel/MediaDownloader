package ru.nbdev.mediadownloader.app;

import javax.inject.Singleton;

import dagger.Component;
import ru.nbdev.mediadownloader.common.image_loader.ImageLoader;
import ru.nbdev.mediadownloader.presenter.DetailPresenter;
import ru.nbdev.mediadownloader.presenter.MainPresenter;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainPresenter mainPresenter);

    void inject(DetailPresenter detailPresenter);

    ImageLoader getImageLoader();
}
