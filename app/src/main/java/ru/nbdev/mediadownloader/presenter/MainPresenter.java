package ru.nbdev.mediadownloader.presenter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

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
import ru.nbdev.mediadownloader.common.image_loader.ImageLoader;
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

    @Inject
    ImageLoader imageLoader;


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
        photosList = null;
        getViewState().showProgress();
        SearchRequest request = new PixabaySearchRequest(query, filter);
        Disposable disposable = photoRepository.searchPhotos(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photos -> {
                    photosList = photos;
                    getViewState().showResult();
                    getViewState().showMessage(R.string.found, String.valueOf(photosList.size()));
                }, throwable -> {
                    getViewState().showError();
                    getViewState().showMessage(R.string.load_error);
                    Timber.e(throwable, "photoRepository.searchPhotos() error.");
                });
        compositeDisposable.add(disposable);
    }

    public MainRecyclerPresenter getMainRecyclerPresenter() {
        return mainRecyclerPresenter;
    }


    private class MainRecyclerPresenter implements RecyclerPresenter {

        @Override
        public int getItemCount() {
            return (photosList != null) ? photosList.size() : 0;
        }

        @Override
        public void bindView(MainRecyclerAdapter.MainRecyclerViewHolderImpl mainRecyclerViewHolder) {
            Timber.d("bindView");
            int position = mainRecyclerViewHolder.getAdapterPosition();
            if (position < 0) {
                return;
            }

            Photo photo = photosList.get(position);
            mainRecyclerViewHolder.showProgress();

            imageLoader.loadImageFromUrl(photo.previewURL, String.valueOf(position), new ImageLoader.OnReadyListener() {

                @Override
                public void onSuccess(Drawable image) {
                    Timber.d("onResourceReady: bind pos %d, id %d, url %s", position, photo.id, photo.previewURL);
                    photo.setDrawable(image);
                    mainRecyclerViewHolder.showPhoto(photo);
                    mainRecyclerViewHolder.setOnImageClickListener(v -> onItemClick(photo.id));
                }

                @Override
                public void onError() {
                    Timber.e("loadImageFromUrl() error.");
                    mainRecyclerViewHolder.showError();
                }
            });
        }

        @Override
        public void onViewRecycled(MainRecyclerAdapter.MainRecyclerViewHolderImpl holder) {
            Timber.d("onViewRecycled");
            int position = holder.getAdapterPosition();
            if (position < 0) {
                return;
            }
            imageLoader.cancelLoading(String.valueOf(position));
        }

        private void onItemClick(long photoId) {
            getViewState().runDetailActivity(photoId);
        }
    }
}
