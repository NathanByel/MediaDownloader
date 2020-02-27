package ru.nbdev.mediadownloader.presenter;

import android.os.Bundle;

import com.google.android.gms.measurement.module.Analytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;
import ru.nbdev.mediadownloader.R;
import ru.nbdev.mediadownloader.app.App;
import ru.nbdev.mediadownloader.model.entity.Photo;
import ru.nbdev.mediadownloader.model.entity.SearchRequest;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabayFilter;
import ru.nbdev.mediadownloader.model.entity.pixabay.PixabaySearchRequest;
import ru.nbdev.mediadownloader.model.repository.PhotoRepository;
import ru.nbdev.mediadownloader.view.main.MainRecyclerAdapter;
import ru.nbdev.mediadownloader.view.main.MainView;
import timber.log.Timber;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    private final MainRecyclerPresenter mainRecyclerPresenter;
    private final CompositeDisposable compositeDisposable;
    private List<Photo> photosList;
    private PixabayFilter pixabayFilter;
    private String lastQuery;

    @Inject
    PhotoRepository photoRepository;

    public MainPresenter() {
        lastQuery = "";
        compositeDisposable = new CompositeDisposable();
        mainRecyclerPresenter = new MainRecyclerPresenter();
        pixabayFilter = new PixabayFilter();
    }

    @Override
    protected void onFirstViewAttach() {
        searchPhotos("", pixabayFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void onRefresh() {
        searchPhotos(lastQuery, pixabayFilter);
    }

    public void onSearch(String query) {
        searchPhotos(query, pixabayFilter);
    }

    public void onFilterIconClick() {
        getViewState().showFilterDialog(pixabayFilter);
    }

    public void onFiltersClosed(PixabayFilter filter) {
        pixabayFilter = filter;
    }

    public void onFiltersShowResultClick(String query, PixabayFilter filter) {
        pixabayFilter = filter;
        getViewState().hideFilterDialog();
        searchPhotos(query, pixabayFilter);
    }

    private void searchPhotos(String query, PixabayFilter filter) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, query);
        App.getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
        
        lastQuery = query;
        clearRecycler();
        getViewState().showProgress();
        SearchRequest request = new PixabaySearchRequest(query, filter);
        Disposable disposable = photoRepository.searchPhotos(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    photosList = photos;
                    getViewState().hideProgress();
                    updateRecycler();
                }, throwable -> {
                    getViewState().showError();
                    getViewState().showMessage(R.string.load_error);
                    Timber.e(throwable, "Repository searchPhotos() error.");
                });
        compositeDisposable.add(disposable);
    }

    private void updateRecycler() {
        if (photosList != null) {
            getViewState().showMessage(R.string.found, String.valueOf(photosList.size()));
            getViewState().updateRecyclerView();
        }
    }

    private void clearRecycler() {
        if (photosList != null) {
            photosList.clear();
            getViewState().updateRecyclerView();
        }
    }

    public MainRecyclerPresenter getMainRecyclerPresenter() {
        return mainRecyclerPresenter;
    }

    private class MainRecyclerPresenter implements RecyclerPresenter {
        @Override
        public int getItemCount() {
            if (photosList != null) {
                return photosList.size();
            }
            return 0;
        }

        @Override
        public void bindView(MainRecyclerAdapter.MainRecyclerViewHolderImpl mainRecyclerViewHolder) {
            Photo photo = photosList.get(mainRecyclerViewHolder.getAdapterPosition());
            mainRecyclerViewHolder.showPhoto(photo);
            mainRecyclerViewHolder.setOnImageClickListener(v -> onItemClick((int) photo.id));
        }

        private void onItemClick(int photoId) {
            getViewState().runDetailActivity(photoId);
        }
    }
}