package ru.nbdev.mediadownloader.app;

import javax.inject.Singleton;

import dagger.Component;
import ru.nbdev.mediadownloader.model.repository.PixabayRepository;
import ru.nbdev.mediadownloader.presenter.DetailPresenter;
import ru.nbdev.mediadownloader.presenter.MainPresenter;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainPresenter mainPresenter);

    void inject(DetailPresenter detailPresenter);

    void inject(PixabayRepository photoRepository);
}
